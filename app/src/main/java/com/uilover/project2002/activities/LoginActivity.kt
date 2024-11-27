package com.uilover.project2002.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.databinding.ActivityLoginBinding
import com.uilover.project2002.viewmodels.LoginViewModel
import com.uilover.project2002.viewmodels.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(this) }

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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
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

        loginViewModel.login(email, password).observe(this, { success ->
            binding.progressBar.visibility = View.GONE
            if (success) {
                val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
                sharedPreferences.edit().putString("logged_in_email", email).apply()
                showToast("Login successful")
                navigateToMain()
            } else {
                showToast("Authentication failed.")
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
