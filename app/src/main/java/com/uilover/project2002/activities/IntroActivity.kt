package com.uilover.project2002.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.databinding.ActivityIntroBinding
import com.uilover.project2002.repositories.MainRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainRepository = MainRepository(this)
        addDataFromCode()

        binding.startBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun addDataFromCode() {
        mainRepository.insertInitialData()
    }
}
