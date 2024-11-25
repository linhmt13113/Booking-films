package com.uilover.project2002.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uilover.project2002.repositories.MainRepository

class MainViewModel(context: Context) : ViewModel() {
    private val mainRepository = MainRepository(context)

    private val _loggedInUserEmail = MutableLiveData<String?>()
    val loggedInUserEmail: LiveData<String?> get() = _loggedInUserEmail

    fun updateUIWithEmail() {
        val sharedPreferences = mainRepository.context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("logged_in_email", null)
        _loggedInUserEmail.postValue(email)
    }

    fun insertInitialData() {
        mainRepository.insertBannerData()
        mainRepository.insertTopMoviesData()
        mainRepository.insertUpcomingMoviesData()
    }

    fun logout() {
        val sharedPreferences = mainRepository.context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("logged_in_email").apply()
        _loggedInUserEmail.postValue(null)
    }

    fun closeDatabase() {
        mainRepository.closeDatabase()
    }
}
