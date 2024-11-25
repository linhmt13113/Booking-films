package com.uilover.project2002.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.databinding.ActivityMainBinding
import com.uilover.project2002.viewmodels.MainViewModel
import com.uilover.project2002.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory(this) }

    override fun onStart() {
        super.onStart()
        mainViewModel.updateUIWithEmail()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        mainViewModel.loggedInUserEmail.observe(this, { email ->
            if (email != null) {
                binding.textView4.text = email
                binding.textView3.text = "Hello ${email.split("@").firstOrNull()}"
            } else {
                navigateToLogin()
            }
        })

        binding.logout.setOnClickListener {
            mainViewModel.logout()
            navigateToLogin()
        }

        mainViewModel.insertInitialData()
    }

    private fun navigateToLogin() {
        val intent = Intent(applicationContext, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.closeDatabase()
    }
}
