package com.uilover.project2002.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.SliderItems
import com.uilover.project2002.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val mainRepository = MainRepository(context)

    private val _loggedInUserEmail = MutableLiveData<String?>()
    val loggedInUserEmail: LiveData<String?> get() = _loggedInUserEmail

    private val _films = MutableLiveData<List<Film>>()
    val films: LiveData<List<Film>> get() = _films

    private val _topMovies = MutableLiveData<List<Film>>()
    val topMovies: LiveData<List<Film>> get() = _topMovies

    private val _sliderItems = MutableLiveData<List<SliderItems>>()
    val sliderItems: LiveData<List<SliderItems>> get() = _sliderItems

    fun updateUIWithEmail() {
        val sharedPreferences = mainRepository.context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("logged_in_email", null)
        _loggedInUserEmail.postValue(email)
    }

    fun insertInitialData() {
        mainRepository.insertBannerData()
        mainRepository.insertTopMovies()
        //mainRepository.insertUpcomingMovies()
        mainRepository.insertFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            val films = mainRepository.getAllFilms()
            _films.postValue(films)
        }
    }

    fun loadTopMovies() {
        viewModelScope.launch {
            val topMovies = mainRepository.getTopMovies()
            _topMovies.postValue(topMovies)
        }
    }

    fun loadSliderItems() {
        viewModelScope.launch {
            val sliderItems = mainRepository.getSliderItems()
            _sliderItems.postValue(sliderItems)
        }
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
