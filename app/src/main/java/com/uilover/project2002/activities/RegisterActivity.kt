package com.uilover.project2002.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uilover.project2002.R
import com.uilover.project2002.databinding.ActivityRegisterBinding
import com.uilover.project2002.viewmodels.RegisterViewModel
import com.uilover.project2002.viewmodels.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(this) }

    public override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("logged_in_email", null)

        if (loggedInUserEmail != null) {
            navigateToMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginNow.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        if (TextUtils.isEmpty(email)) {
            showToast("Enter email")
            binding.progressBar.visibility = View.GONE
            return
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Enter password")
            binding.progressBar.visibility = View.GONE
            return
        }

        registerViewModel.register(email, password).observe(this, { success ->
            binding.progressBar.visibility = View.GONE
            if (success) {
                showToast("Account created successfully")
                navigateToMain()
            } else {
                showToast("User already exists")
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
