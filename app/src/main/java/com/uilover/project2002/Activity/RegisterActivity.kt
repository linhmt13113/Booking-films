package com.uilover.project2002.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.uilover.project2002.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button

    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var dbHelper: DatabaseHelper

    public override fun onStart() {
        super.onStart()
//        val loggedInUserEmail = dbHelper.getLoggedInUser()
//        if (loggedInUserEmail != null) {
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.loginNow)


        textView.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }



            // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
            if (dbHelper.checkUserExists(email)) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            } else {
                // Lưu thông tin người dùng vào SQLite
                dbHelper.insertUser(email, password)
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("logged_in_email")  // Lưu email người dùng
                editor.apply()


                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}