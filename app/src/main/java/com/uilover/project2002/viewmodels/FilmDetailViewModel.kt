package com.uilover.project2002.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.repositories.FilmDetailRepository

class FilmDetailViewModel(context: Context) : ViewModel() {
    private val filmDetailRepository = FilmDetailRepository(context)
    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film> get() = _film

    fun setFilm(item: Film) {
        _film.value = item
    }
}