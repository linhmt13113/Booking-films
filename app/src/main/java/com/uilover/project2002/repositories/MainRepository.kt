package com.uilover.project2002.repositories

import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.data.model.Cast
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.User

class MainRepository(val context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val RESET_DATABASE = true

    fun handleDatabase() {
        if (RESET_DATABASE) {
            dbHelper.resetDatabase()
            insertInitialData()
        }
    }

    fun insertInitialData() {
        insertFilms()
        insertUpcomingMovies()
    }

    fun insertFilms() {
        val films = listOf(
            Film(
                title = "Naruto",
                description = "Inception is a science fiction film directed by Christopher Nolan",
                poster = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
                time = "148 min",
                trailer = "https://youtu.be/YoHD9XEInc0",
                imdb = 87,
                year = 2010,
                price = 12.0,
                genre = arrayListOf("Action", "Sci-Fi"),
                casts = arrayListOf(Cast(actor = "Leonardo DiCaprio, "), Cast(actor = "Joseph Gordon-Levitt"))
            ),
            Film(
                title = "Boruto",
                description = "Interstellar is a science fiction film directed by Christopher Nolan",
                poster = "https://upload.wikimedia.org/wikipedia/vi/4/46/Interstellar_poster.jpg",
                time = "169 min",
                trailer = "https://youtu.be/zSWdZVtXT7E",
                imdb = 89,
                year = 2014,
                price = 15.0,
                genre = arrayListOf("Adventure", "Drama", "Sci-Fi"),
                casts = arrayListOf(Cast(actor = "Matthew McConaughey, "), Cast(actor = "Anne Hathaway"))
            ),
            Film(
                title = "The Dark Knight",
                description = "The Dark Knight is a superhero film directed by Christopher Nolan",
                poster = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                time = "152 min",
                trailer = "https://youtu.be/EXeTwQWrcwY",
                imdb = 90,
                year = 2008,
                price = 14.0,
                genre = arrayListOf("Action", "Crime", "Drama"),
                casts = arrayListOf(Cast(actor = "Christian Bale, "), Cast(actor = "Heath Ledger"))
            ),
            Film(
                title = "The Matrix",
                description = "The Matrix is a science fiction film directed by the Wachowskis",
                poster = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                time = "136 min",
                trailer = "https://youtu.be/vKQi3bBA1y8",
                imdb = 87,
                year = 1999,
                price = 10.0,
                genre = arrayListOf("Action", "Sci-Fi"),
                casts = arrayListOf(Cast(actor = "Keanu Reeves, "), Cast(actor = "Laurence Fishburne"))
            ),
            Film(
                title = "The Shawshank Redemption",
                description = "The Shawshank Redemption is a drama film directed by Frank Darabont",
                poster = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                time = "142 min",
                trailer = "https://youtu.be/6hB3S9bIaco",
                imdb = 91,
                year = 1994,
                price = 13.0,
                genre = arrayListOf("Drama"),
                casts = arrayListOf(Cast(actor = "Tim Robbins, "), Cast(actor = "Morgan Freeman"))
            )
        )

        films.forEach { film ->
            if (!dbHelper.doesFilmExist(film.title)) {
                dbHelper.insertFilm(film)
            }
        }
    }

    fun insertUpcomingMovies() {
        val upcomingMovies = listOf(
            Film(
                title = "Dune: Part Two",
                description = "The second part of Denis Villeneuve's adaptation of Frank Herbert's science fiction novel.",
                poster = "https://upload.wikimedia.org/wikipedia/en/5/52/Dune_Part_Two_poster.jpeg"
            ),
            Film(
                title = "Avatar 3",
                description = "The third installment in James Cameron's Avatar film series.",
                poster = "https://cdn.kinocheck.com/i/4k6tszhp25.jpg"
            )
        )
        upcomingMovies.forEach { movie ->
            if (!dbHelper.doesUpcomingFilmExist(movie.title)) {
                dbHelper.insertUpcomingMovie(movie)
            }
        }
    }

    fun refreshDatabase() {
        dbHelper.resetDatabase()
        insertInitialData()
    }


    fun getUserByEmail(email: String): User? {
        return dbHelper.getUserByEmail(email)
    }

    fun getAllFilms(): List<Film> {
        return dbHelper.getAllFilms()
    }

    fun getUpcomingMovies(): List<Film> {
        return dbHelper.getUpcomingMovies()
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
