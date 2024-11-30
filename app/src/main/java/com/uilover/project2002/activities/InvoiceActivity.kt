package com.uilover.project2002.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.data.model.Invoice
import com.uilover.project2002.databinding.ActivityInvoiceBinding

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val filmTitle = intent.getStringExtra("filmTitle")
        val showDate = intent.getStringExtra("showDate")
        val showTime = intent.getStringExtra("showTime")
        val seats = intent.getStringExtra("seats")
        val cinemaHall = intent.getIntExtra("cinemaHall", 1)
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val email = intent.getStringExtra("email")
        val barcode = generateRandomBarcode()

        binding.filmName.text = filmTitle
        binding.date.text = showDate
        binding.time.text = showTime
        binding.seats.text = seats
        binding.cinemaId.text = "Cinema Hall: $cinemaHall"
        binding.price.text = "Total Price: $totalPrice"
        binding.email.text = "Email: $email"
        binding.barcode.text = "Barcode: $barcode"

        // Lưu hóa đơn khi người dùng nhấn nút Confirm
        binding.buttonConfirm.setOnClickListener {
            val invoice = Invoice(
                filmTitle = filmTitle ?: "",
                showDate = showDate ?: "",
                showTime = showTime ?: "",
                seats = seats ?: "",
                cinemaHall = cinemaHall,
                totalPrice = totalPrice,
                email = email ?: "",
                barcode = barcode
            )
            dbHelper.insertInvoice(invoice)
            Toast.makeText(this, "Invoice saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateRandomBarcode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }
}
