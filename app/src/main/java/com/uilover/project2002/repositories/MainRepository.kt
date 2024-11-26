package com.uilover.project2002.repositories

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.local.DatabaseHelper
import com.uilover.project2002.data.model.Cast
import com.uilover.project2002.data.model.SliderItems
import java.io.BufferedReader
import java.io.InputStreamReader

class MainRepository(val context: Context) {
    private val dbHelper = DatabaseHelper(context)

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


    fun insertTopMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Top Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "https://example.com/top_movie.jpg")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Top Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_TOP_MOVIES, null, values)
    }

    fun insertUpcomingMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Upcoming Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "https://example.com/upcoming_movie.jpg")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Upcoming Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_UPCOMING, null, values)
    }

    fun insertFilmsFromCSV() {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.query(DatabaseHelper.TABLE_FILMS, null, null, null, null, null, null)
            if (cursor.count > 0) {
                Log.d("CSV Import", "Data already exists. Skipping import.")
                cursor.close()
                return
            }
            cursor.close()

            val inputStream = context.assets.open("films.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use { br ->
                var lineIndex = 0
                br.forEachLine { line ->
                    if (lineIndex == 0) {
                        lineIndex++
                        return@forEachLine
                    }
                    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    Log.d("CSV Line", "Line $lineIndex: $line")
                    if (parts.size == 10) {
                        val film = Film(
                            title = parts[0],
                            description = parts[1],
                            poster = parts[2],
                            time = parts[3],
                            trailer = parts[4],
                            imdb = parts[5].toInt(),
                            year = parts[6].toInt(),
                            price = parts[7].toDouble(),
                            genre = ArrayList(parts[8].replace("\"", "").split(";")),
                            casts = ArrayList(
                                parts[9].replace("\"", "").split(";").map { Cast(it) })
                        )
                        Log.d("CSV Film", "Film $lineIndex: $film")
                        val db = dbHelper.writableDatabase
                        val values = ContentValues().apply {
                            put(DatabaseHelper.COLUMN_TITLE, film.title)
                            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
                            put(DatabaseHelper.COLUMN_IMAGE_URL, film.poster)
                            put(DatabaseHelper.COLUMN_PRICE, film.price)
                        }
                        db.insert(DatabaseHelper.TABLE_FILMS, null, values)
                    } else {
                        Log.e("CSV Error", "Invalid data format in line $lineIndex")
                    }
                    lineIndex++
                }
            }
        } catch (e: Exception) {
            Log.e("CSV Error", "Error reading CSV file: ${e.message}")
            e.printStackTrace()
        }
    }

    fun getAllFilms(): List<Film> {
        return dbHelper.getAllFilms()
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
