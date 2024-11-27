package com.uilover.project2002.repositories

import android.content.ContentValues
import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.data.model.Cast
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.SliderItems

class MainRepository(val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertInitialData() {
        insertFilms()
        insertTopMovies()
    }

    fun insertFilms() {
        val films = listOf(
            Film(
                title = "Inception",
                description = "Inception is a science fiction film directed by Christopher Nolan",
                poster = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
                time = "148 min",
                trailer = "https://youtu.be/YoHD9XEInc0",
                imdb = 87,
                year = 2010,
                price = 12.0,
                genre = arrayListOf("Action", "Sci-Fi"),
                casts = arrayListOf(Cast(actor = "Leonardo DiCaprio"), Cast(actor = "Joseph Gordon-Levitt"))
            ),
            Film(
                title = "Interstellar",
                description = "Interstellar is a science fiction film directed by Christopher Nolan",
                poster = "https://upload.wikimedia.org/wikipedia/vi/4/46/Interstellar_poster.jpg",
                time = "169 min",
                trailer = "https://youtu.be/zSWdZVtXT7E",
                imdb = 89,
                year = 2014,
                price = 15.0,
                genre = arrayListOf("Adventure", "Drama", "Sci-Fi"),
                casts = arrayListOf(Cast(actor = "Matthew McConaughey"), Cast(actor = "Anne Hathaway"))
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
                casts = arrayListOf(Cast(actor = "Christian Bale"), Cast(actor = "Heath Ledger"))
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
                casts = arrayListOf(Cast(actor = "Keanu Reeves"), Cast(actor = "Laurence Fishburne"))
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
                casts = arrayListOf(Cast(actor = "Tim Robbins"), Cast(actor = "Morgan Freeman"))
            )
        )

        films.forEach { dbHelper.insertFilm(it) }
    }

    fun insertTopMovies() {
        val topMovies = listOf(
            Film(
                title = "Inception",
                description = "Inception is a science fiction film directed by Christopher Nolan",
                poster = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
                price = 12.0
            ),
            Film(
                title = "Interstellar",
                description = "Interstellar is a science fiction film directed by Christopher Nolan",
                poster = "https://upload.wikimedia.org/wikipedia/vi/4/46/Interstellar_poster.jpg",
                price = 15.0
            ),
            Film(
                title = "The Dark Knight",
                description = "The Dark Knight is a superhero film directed by Christopher Nolan",
                poster = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                price = 14.0
            )
        )

        topMovies.forEach { dbHelper.insertTopMovie(it) }
    }

    fun insertBannerData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Banner Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "https://example.com/banner.jpg")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Banner Description")
        }
        db.insert(DatabaseHelper.TABLE_BANNERS, null, values)
    }

    fun getSliderItems(): List<SliderItems> {
        return dbHelper.getSliderItems()
    }

//    fun insertUpcomingMovies() {
//        val upcomingMovies = listOf(
//            Film(
//                title = "Dune: Part Two",
//                description = "The second part of Denis Villeneuve's adaptation of Frank Herbert's science fiction novel.",
//                poster = "https://example.com/dune_part_two.jpg", time = null,
//                trailer = "https://youtu.be/8g18jFHCLXk",
//                imdb = 0,
//                year = 2023,
//                price = 0.0,
//                genre = arrayListOf("Action", "Adventure", "Sci-Fi"),
//                casts = arrayListOf(Cast(actor = "Timothée Chalamet"),
//                    Cast(actor = "Zendaya")) ),
//            Film(
//                title = "Avatar 3",
//                description = "The third installment in James Cameron's Avatar film series.",
//                poster = "https://example.com/avatar_3.jpg", time = null,
//                trailer = null,
//                imdb = 0, year = 2024,
//                price = 0.0,
//                genre = arrayListOf("Action", "Adventure", "Fantasy"),
//                casts = arrayListOf(Cast(actor = "Sam Worthington"),
//                    Cast(actor = "Zoe Saldana")) )
//    // Thêm các phim sắp chiếu khác tại đây
//            ) upcomingMovies.forEach { dbHelper.insertUpcomingMovie(it) } }
//    }

    fun getAllFilms(): List<Film> {
        return dbHelper.getAllFilms()
    }

    fun getTopMovies(): List<Film> {
        return dbHelper.getTopMovies()
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
