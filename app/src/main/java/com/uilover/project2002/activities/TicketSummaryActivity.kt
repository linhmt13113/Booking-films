package com.uilover.project2002.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.databinding.ActivityTicketSummaryBinding

class TicketSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketSummaryBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filmTitle = intent.getStringExtra("filmTitle")
        val showDate = intent.getStringExtra("showDate")
        val showTime = intent.getStringExtra("showTime")
        val seats = intent.getStringExtra("seats")
        val cinemaHall = intent.getIntExtra("cinemaHall", 1)
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val email = intent.getStringExtra("email")

        binding.textViewFilmTitle.text = "Film Title: $filmTitle"
        binding.textViewShowDate.text = "Date: $showDate"
        binding.textViewShowTime.text = "Time: $showTime"
        binding.textViewSeats.text = "Seats: $seats"
        binding.textViewCinemaHall.text = "Cinema Hall: $cinemaHall"
        binding.textViewTotalPrice.text = "Total Price: $$totalPrice"

        binding.buttonConfirm.setOnClickListener {
            val intent = Intent(this, InvoiceActivity::class.java).apply {
                putExtra("filmTitle", filmTitle)
                putExtra("showDate", showDate)
                putExtra("showTime", showTime)
                putExtra("seats", seats)
                putExtra("cinemaHall", cinemaHall)
                putExtra("totalPrice", totalPrice)
                putExtra("email", email)
            }
            startActivity(intent)
        }

        binding.buttonCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
