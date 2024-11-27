package com.uilover.project2002.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.SliderItems

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_BANNERS = "banners"
        const val TABLE_TOP_MOVIES = "top_movies"
        const val TABLE_UPCOMING = "upcoming"
        const val TABLE_USER = "user"
        const val TABLE_FILMS = "films"
        const val TABLE_SLIDER_ITEMS = "slider_items"

        const val COLUMN_USER_ID = "id"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_TITLE = "title"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_USER ($COLUMN_USER_ID INTEGER PRIMARY KEY, $COLUMN_USER_EMAIL TEXT, $COLUMN_USER_PASSWORD TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_BANNERS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_TOP_MOVIES ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_PRICE REAL)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_UPCOMING ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_FILMS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_PRICE REAL)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_SLIDER_ITEMS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BANNERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TOP_MOVIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_UPCOMING")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FILMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SLIDER_ITEMS")
        onCreate(db)
    }

    fun insertUser(email: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_PASSWORD, password)
        }
        db.insert(TABLE_USER, null, values)
    }

    fun checkUserLogin(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USER, null,
            "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?", arrayOf(email, password),
            null, null, null
        )
        return cursor.count > 0
    }

    fun checkUserExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER, null,
            "$COLUMN_USER_EMAIL = ?", arrayOf(email),
            null, null, null
        )
        return cursor.count > 0
    }

    fun getLoggedInUser(): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER, arrayOf(COLUMN_USER_EMAIL),
            null, null, null, null, null
        )
        val columnIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL)
        return if (cursor.moveToFirst() && columnIndex >= 0) {
            cursor.getString(columnIndex)
        } else {
            null
        }
    }

    fun getUser(): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_USER, arrayOf(COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD),
            null, null, null, null, null
        )
    }

    fun insertFilm(film: Film) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, film.title)
            put(COLUMN_DESCRIPTION, film.description)
            put(COLUMN_IMAGE_URL, film.poster)
            put(COLUMN_PRICE, film.price)
        }
        db.insert(TABLE_FILMS, null, values)
    }

    fun insertTopMovie(film: Film) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, film.title)
            put(COLUMN_DESCRIPTION, film.description)
            put(COLUMN_IMAGE_URL, film.poster)
            put(COLUMN_PRICE, film.price)
        }
        db.insert(TABLE_TOP_MOVIES, null, values)
    }

    fun getAllFilms(): List<Film> {
        val films = mutableListOf<Film>()
        val db = readableDatabase
        val cursor = db.query(TABLE_FILMS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                val posterIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL)
                val priceIndex = cursor.getColumnIndex(COLUMN_PRICE)

                val film = Film(
                    title = if (titleIndex >= 0) cursor.getString(titleIndex) else "",
                    description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else "",
                    poster = if (posterIndex >= 0) cursor.getString(posterIndex) else "",
                    time = null,
                    trailer = null,
                    imdb = 0,
                    year = 0,
                    price = if (priceIndex >= 0) cursor.getDouble(priceIndex) else 0.0,
                    genre = arrayListOf(),
                    casts = arrayListOf()
                )
                films.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return films
    }

    fun getTopMovies(): List<Film> {
        val topMovies = mutableListOf<Film>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TOP_MOVIES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                val posterIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL)
                val priceIndex = cursor.getColumnIndex(COLUMN_PRICE)

                val film = Film(
                    title = if (titleIndex >= 0) cursor.getString(titleIndex) else "",
                    description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else "",
                    poster = if (posterIndex >= 0) cursor.getString(posterIndex) else "",
                    time = null,
                    trailer = null,
                    imdb = 0,
                    year = 0,
                    price = if (priceIndex >= 0) cursor.getDouble(priceIndex) else 0.0,
                    genre = arrayListOf(),
                    casts = arrayListOf()
                )
                topMovies.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return topMovies
    }

    fun insertUpcomingMovie(film: Film) {
        val db = writableDatabase
        val values = ContentValues().apply { put(COLUMN_TITLE, film.title)
            put(COLUMN_DESCRIPTION, film.description)
            put(COLUMN_IMAGE_URL, film.poster) }
        db.insert(TABLE_UPCOMING, null, values)
    }


    fun getSliderItems(): List<SliderItems> {
        val sliderItems = mutableListOf<SliderItems>()
        val db = readableDatabase
        val cursor = db.query(TABLE_SLIDER_ITEMS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)

                val sliderItem = SliderItems(
                    title = if (titleIndex >= 0) cursor.getString(titleIndex) else "",
                    imageUrl = if (imageUrlIndex >= 0) cursor.getString(imageUrlIndex) else "",
                    description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else ""
                )
                sliderItems.add(sliderItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sliderItems
    }
}
