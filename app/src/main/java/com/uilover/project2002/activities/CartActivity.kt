package com.uilover.project2002.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uilover.project2002.R
import com.uilover.project2002.adapters.InvoiceListAdapter
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.data.model.Invoice
import com.uilover.project2002.databinding.ActivityCartBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var dbHelper: DatabaseHelper
    private val selectedInvoices = mutableListOf<Invoice>()
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val email = getEmailFromSharedPreferences() // Lấy email từ SharedPreferences

        if (email != null) {
            val invoices = dbHelper.getAllInvoices(email)

            // Kiểm tra xem có hóa đơn hay không
            if (invoices.isNotEmpty()) {
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = InvoiceListAdapter(invoices) { invoice, isChecked ->
                    if (isChecked) {
                        selectedInvoices.add(invoice)
                    } else {
                        selectedInvoices.remove(invoice)
                    }
                    updateDeleteButtonVisibility()
                }
            } else {
                Toast.makeText(this, "No invoices found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        binding.buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        binding.buttonEdit.setOnClickListener {
            isEditMode = !isEditMode
            updateEditMode()
        }

        // Xử lý sự kiện cho ChipNavigationBar
        val navBar: ChipNavigationBar = findViewById(R.id.chipNavigationBar)
        navBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_cart -> {
                    Toast.makeText(this, "You are already in Cart", Toast.LENGTH_SHORT).show()
                }
                // R.id.nav_profile -> {
                //     val intent = Intent(this, ProfileActivity::class.java)
                //     startActivity(intent)
                // }
            }
        }
    }

    private fun getEmailFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        return sharedPreferences.getString("logged_in_email", null)
    }

    private fun updateDeleteButtonVisibility() {
        binding.buttonDelete.visibility = if (selectedInvoices.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete the selected invoices?")
            .setPositiveButton("Confirm") { dialog, which ->
                deleteSelectedInvoices()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteSelectedInvoices() {
        selectedInvoices.forEach { invoice ->
            dbHelper.deleteInvoice(invoice) // Thêm hàm deleteInvoice trong DatabaseHelper
        }
        selectedInvoices.clear()
        recreate() // Làm mới Activity để cập nhật danh sách hóa đơn
        Toast.makeText(this, "Invoices deleted successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateEditMode() {
        val adapter = binding.recyclerView.adapter as InvoiceListAdapter
        adapter.setEditMode(isEditMode)
    }
}