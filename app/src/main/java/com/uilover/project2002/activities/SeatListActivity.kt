package com.uilover.project2002.activities

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.uilover.project2002.adapters.DateAdapter
import com.uilover.project2002.adapters.SeatListAdapter
import com.uilover.project2002.adapters.TimeAdapter
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.Seat
import com.uilover.project2002.databinding.ActivitySeatListBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.random.Random
import java.util.Locale

class SeatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeatListBinding
    private lateinit var film: Film
    private var price: Double = 0.0
    private var number: Int = 0
    private var selectedDate: LocalDate? = null
    private var selectedTime: LocalTime? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            getIntentExtra()
            setVariable()
            initDateAndTimePickers()

            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } catch (e: Exception) {
            Log.e("SeatListActivity", "Error initializing activity", e)
        }

        binding.button.setOnClickListener {
            val seats = (binding.seatRecyclerview.adapter as SeatListAdapter).getSelectedSeats()
            if (selectedDate != null && selectedTime != null && seats.isNotEmpty()) {
                val email = getEmailFromSharedPreferences()
                val intent = Intent(this, TicketSummaryActivity::class.java).apply {
                    putExtra("filmTitle", film.title)
                    putExtra("showDate", selectedDate.toString())
                    putExtra("showTime", selectedTime.toString())
                    putExtra("seats", seats.joinToString(","))
                    putExtra("cinemaHall", 1) // Example: cinema hall based on film ID
                    putExtra("totalPrice", price)
                    putExtra("email", email)
                }
                startActivity(intent)
            } else {
                // Hiển thị thông báo lỗi nếu người dùng chưa chọn đầy đủ thông tin
                Log.e("SeatListActivity", "Missing information: Date, Time, or Seats not selected")
            }
        }
    }

    private fun getEmailFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        return sharedPreferences.getString("logged_in_email", null)
    }

    private fun initDateAndTimePickers() {
        val dateFormatter = DateTimeFormatter.ofPattern("EEE/dd/MMM", Locale.ENGLISH)

        binding.dateRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val dates = generateDates().map { it.format(dateFormatter) }
        binding.dateRecyclerview.adapter = DateAdapter(dates) { dateStr ->
            try {
                val parsedDate = parseDateString(dateStr)
                selectedDate = parsedDate
                updateAvailableTimeSlots(parsedDate)
                binding.TimeRecyclerview.visibility = View.VISIBLE
                binding.seatRecyclerview.visibility = View.GONE  // Hide the seat list when a new date is selected
            } catch (e: Exception) {
                Log.e("SeatListActivity", "Error parsing date: $dateStr", e)
            }
        }

        // Initial visibility settings
        binding.TimeRecyclerview.visibility = View.GONE
        binding.seatRecyclerview.visibility = View.GONE
    }

    private fun parseDateString(dateString: String): LocalDate {
        val parts = dateString.split("/")
        val dayOfWeek = parts[0]
        val dayOfMonth = parts[1].toInt()
        val monthAbbreviation = parts[2].toUpperCase(Locale.ENGLISH)

        // Map three-letter month abbreviation to Month enum
        val month = when (monthAbbreviation) {
            "JAN" -> Month.JANUARY
            "FEB" -> Month.FEBRUARY
            "MAR" -> Month.MARCH
            "APR" -> Month.APRIL
            "MAY" -> Month.MAY
            "JUN" -> Month.JUNE
            "JUL" -> Month.JULY
            "AUG" -> Month.AUGUST
            "SEP" -> Month.SEPTEMBER
            "OCT" -> Month.OCTOBER
            "NOV" -> Month.NOVEMBER
            "DEC" -> Month.DECEMBER
            else -> throw IllegalArgumentException("Invalid month abbreviation: $monthAbbreviation")
        }

        return LocalDate.of(LocalDate.now().year, month, dayOfMonth)
    }

    private fun generateDates(): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        val today = LocalDate.now()

        for (i in 0 until 7) {
            dates.add(today.plusDays(i.toLong()))
        }
        Log.d("SeatListActivity", "Generated Dates: $dates")
        return dates
    }



    private fun updateAvailableTimeSlots(date: LocalDate) {
        val timeSlots = generateTimeSlots()
        val currentTime = LocalTime.now()

        // Filter out past times only if the selected date is today
        val upcomingTimeSlots = mutableListOf<String>()
        if (date.isEqual(LocalDate.now())) {
            for (time in timeSlots) {
                val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
                if (localTime.isAfter(currentTime.plusMinutes(30))) {
                    upcomingTimeSlots.add(time)
                }
            }
        } else {
            // For future dates, include all time slots
            upcomingTimeSlots.addAll(timeSlots)
        }

        Log.d("SeatListActivity", "Upcoming Time Slots for $date: $upcomingTimeSlots")

        binding.TimeRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.TimeRecyclerview.adapter = TimeAdapter(upcomingTimeSlots) {
            selectedTime = LocalTime.parse(it, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
            updateSeatsList()
            binding.seatRecyclerview.visibility = View.VISIBLE // Show the seat list when a time slot is selected
        }
    }

    private fun updateSeatsList() {
        val gridLayoutManager = GridLayoutManager(this, 8)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }

        binding.seatRecyclerview.layoutManager = gridLayoutManager

        try {
            val seatAdapter = SeatListAdapter(generateSeats(), object : SeatListAdapter.SelectedSeat {
                override fun returnSelected(selectedName: String, num: Int) {
                    binding.numberSelectedTxt.text = "$num Seat Selected"
                    val df = DecimalFormat("#.##")
                    price = df.format(num * film.price).toDouble()
                    number = num
                    binding.priceTxt.text = "$$price"
                }
            })
            binding.seatRecyclerview.adapter = seatAdapter
            binding.seatRecyclerview.isNestedScrollingEnabled = false
        } catch (e: Exception) {
            Log.e("SeatListActivity", "Error updating seats list", e)
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun getIntentExtra() {
        film = intent.getParcelableExtra("film")!!
    }

    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val start = LocalTime.of(10, 0) // 10:00 AM
        val end = LocalTime.of(23, 59) // 11:59 PM
        var time = start

        // Limit the number of time slots to avoid OOM
        while (time.isBefore(end) && timeSlots.size < 10) { // Ensure no more than 10 slots
            timeSlots.add(time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)))
            time = time.plusHours(2)
        }

        Log.d("SeatListActivity", "Generated Time Slots: $timeSlots")

        return timeSlots
    }

    private fun generateSeats(): List<Seat> {
        val seatList = mutableListOf<Seat>()
        val numberSeats = 96 // 12 rows and 8 columns = 96 seats

        if (selectedDate != null && selectedTime != null) {
            val currentTime = LocalTime.now()

            for (i in 0 until numberSeats) {
                val row = 'A' + (i / 8)
                val seatName = "$row${i % 8 + 1}"
                val seatStatus = when {
                    // Nearest two days with some seats booked in pairs
                    selectedDate!!.isEqual(LocalDate.now()) || selectedDate!!.isEqual(LocalDate.now().plusDays(1)) -> {
                        if (i % 8 < 7 && Random.nextBoolean()) {
                            Seat.SeatStatus.UNAVAILABLE
                        } else {
                            Seat.SeatStatus.AVAILABLE
                        }
                    }
                    // Showtimes that have passed by more than 30 minutes are sold out
                    selectedDate!!.isEqual(LocalDate.now()) && selectedTime!!.plusMinutes(30).isBefore(currentTime) -> {
                        Seat.SeatStatus.UNAVAILABLE
                    }
                    // Future dates with all seats available
                    else -> Seat.SeatStatus.AVAILABLE
                }
                seatList.add(Seat(seatStatus, seatName))
            }
        }

        Log.d("SeatListActivity", "Generated Seats: $seatList")

        return seatList
    }
}
