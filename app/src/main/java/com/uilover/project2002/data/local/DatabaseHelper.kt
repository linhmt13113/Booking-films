package com.uilover.project2002.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uilover.project2002.data.model.Cast
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.Invoice
import com.uilover.project2002.data.model.SliderItems

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_UPCOMING = "upcoming"
        const val TABLE_FILMS = "films"
        const val TABLE_USER = "user"
        const val TABLE_SLIDER_ITEMS = "slider_items"
        const val TABLE_INVOICES = "invoices"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_PRICE = "price"
        const val COLUMN_TIME = "time"
        const val COLUMN_TRAILER = "trailer"
        const val COLUMN_IMDB = "imdb"
        const val COLUMN_YEAR = "year"
        const val COLUMN_RELEASE_DATE = "release_date"
        const val COLUMN_GENRE = "genre"
        const val COLUMN_CASTS = "casts"

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USERNAME = "username"

        const val COLUMN_SHOW_DATE = "show_date"
        const val COLUMN_SHOW_TIME = "show_time"
        const val COLUMN_SEATS = "seats"
        const val COLUMN_CINEMA_HALL = "cinema_hall"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_BARCODE = "barcode"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_USER ($COLUMN_USER_ID INTEGER PRIMARY KEY, $COLUMN_USER_EMAIL TEXT, $COLUMN_USERNAME TEXT, $COLUMN_USER_PASSWORD TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_FILMS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_PRICE REAL, $COLUMN_TIME TEXT, $COLUMN_TRAILER TEXT, $COLUMN_IMDB INTEGER, $COLUMN_YEAR INTEGER, $COLUMN_GENRE TEXT, $COLUMN_CASTS TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_UPCOMING ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_RELEASE_DATE TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_SLIDER_ITEMS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_INVOICES ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_SHOW_DATE TEXT, $COLUMN_SHOW_TIME TEXT, $COLUMN_SEATS TEXT, $COLUMN_CINEMA_HALL INTEGER, $COLUMN_TOTAL_PRICE REAL, $COLUMN_USER_EMAIL TEXT, $COLUMN_BARCODE TEXT, $COLUMN_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FILMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_UPCOMING")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SLIDER_ITEMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_INVOICES")
        onCreate(db)
    }

    fun insertInvoice(invoice: Invoice) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, invoice.filmTitle)
            put(COLUMN_SHOW_DATE, invoice.showDate)
            put(COLUMN_SHOW_TIME, invoice.showTime)
            put(COLUMN_SEATS, invoice.seats)
            put(COLUMN_CINEMA_HALL, invoice.cinemaHall)
            put(COLUMN_TOTAL_PRICE, invoice.totalPrice)
            put(COLUMN_USER_EMAIL, invoice.email)
            put(COLUMN_BARCODE, invoice.barcode)
        }
        db.insert(TABLE_INVOICES, null, values)
    }

    fun getAllInvoices(email: String): List<Invoice> {
        val invoices = mutableListOf<Invoice>()
        val db = readableDatabase
        val cursor = db.query(TABLE_INVOICES, null, "$COLUMN_USER_EMAIL = ?", arrayOf(email), null, null, "$COLUMN_TIMESTAMP DESC")
        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val showDateIndex = cursor.getColumnIndex(COLUMN_SHOW_DATE)
                val showTimeIndex = cursor.getColumnIndex(COLUMN_SHOW_TIME)
                val seatsIndex = cursor.getColumnIndex(COLUMN_SEATS)
                val cinemaHallIndex = cursor.getColumnIndex(COLUMN_CINEMA_HALL)
                val totalPriceIndex = cursor.getColumnIndex(COLUMN_TOTAL_PRICE)
                val emailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL)
                val barcodeIndex = cursor.getColumnIndex(COLUMN_BARCODE)

                val invoice = Invoice(
                    filmTitle = if (titleIndex >= 0) cursor.getString(titleIndex) else "",
                    showDate = if (showDateIndex >= 0) cursor.getString(showDateIndex) else "",
                    showTime = if (showTimeIndex >= 0) cursor.getString(showTimeIndex) else "",
                    seats = if (seatsIndex >= 0) cursor.getString(seatsIndex) else "",
                    cinemaHall = if (cinemaHallIndex >= 0) cursor.getInt(cinemaHallIndex) else 0,
                    totalPrice = if (totalPriceIndex >= 0) cursor.getDouble(totalPriceIndex) else 0.0,
                    email = if (emailIndex >= 0) cursor.getString(emailIndex) else "",
                    barcode = if (barcodeIndex >= 0) cursor.getString(barcodeIndex) else ""
                )
                invoices.add(invoice)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return invoices
    }

    fun insertUser (username: String, email: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
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

    fun getLoggedInUser (): String? {
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

    fun getUser (): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_USER, arrayOf(COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD),
            null, null, null, null, null
        )
    }

    fun insertFilm(film: Film) {
        val db = writableDatabase
        if (!doesFilmExist(film.title)) {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, film.title)
                put(COLUMN_DESCRIPTION, film.description)
                put(COLUMN_IMAGE_URL, film.poster)
                put(COLUMN_PRICE, film.price)
                put(COLUMN_TIME, film.time ?: "")
                put(COLUMN_TRAILER, film.trailer ?: "")
                put(COLUMN_IMDB, film.imdb)
                put(COLUMN_YEAR, film.year)
                put(COLUMN_GENRE, film.genre.filterNot { it.isNullOrEmpty() }.joinToString(","))
                put(COLUMN_CASTS, film.casts.map { it.actor }.filterNot { it.isNullOrEmpty() }.joinToString(","))
            }
            db.insert(TABLE_FILMS, null, values)
        }
    }

    fun insertUpcomingMovie(film: Film) {
        val db = writableDatabase
        if (!doesUpcomingFilmExist(film.title)) {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, film.title)
                put(COLUMN_DESCRIPTION, film.description)
                put(COLUMN_IMAGE_URL, film.poster)
            }
            db.insert(TABLE_UPCOMING, null, values)
        }
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
                val timeIndex = cursor.getColumnIndex(COLUMN_TIME)
                val trailerIndex = cursor.getColumnIndex(COLUMN_TRAILER)
                val imdbIndex = cursor.getColumnIndex(COLUMN_IMDB)
                val yearIndex = cursor.getColumnIndex(COLUMN_YEAR)
                val genreIndex = cursor.getColumnIndex(COLUMN_GENRE)
                val castsIndex = cursor.getColumnIndex(COLUMN_CASTS)

                val film = Film(
                    title = if (titleIndex >= 0) cursor.getString(titleIndex) ?: "" else "",
                    description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) ?: "" else "",
                    poster = if (posterIndex >= 0) cursor.getString(posterIndex) ?: "" else "",
                    time = if (timeIndex >= 0) cursor.getString(timeIndex) else null,
                    trailer = if (trailerIndex >= 0) cursor.getString(trailerIndex) else null,
                    imdb = if (imdbIndex >= 0) cursor.getInt(imdbIndex) else 0,
                    year = if (yearIndex >= 0) cursor.getInt(yearIndex) else 0,
                    price = if (priceIndex >= 0) cursor.getDouble(priceIndex) else 0.0,
                    genre = if (genreIndex >= 0) cursor.getString(genreIndex)?.split(",")?.filterNot { it.isNullOrEmpty() }?.toCollection(ArrayList()) ?: arrayListOf() else arrayListOf(),
                    casts = if (castsIndex >= 0) cursor.getString(castsIndex)?.split(",")?.map { Cast(actor = it) }?.toCollection(ArrayList()) ?: arrayListOf() else arrayListOf()
                )
                films.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return films
    }

    fun getUpcomingMovies(): List<Film> {
        val upcomingMovies = mutableListOf<Film>()
        val db = readableDatabase
        val cursor = db.query(TABLE_UPCOMING, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                val posterIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL)

                val film = Film(
                    title = if (titleIndex >= 0) cursor.getString(titleIndex) else "",
                    description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else "",
                    poster = if (posterIndex >= 0) cursor.getString(posterIndex) else "",
                )
                upcomingMovies.add(film)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return upcomingMovies
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

    fun doesFilmExist(title: String?): Boolean {
        if (title == null) return false
        val db = readableDatabase
        val cursor = db.query(TABLE_FILMS, arrayOf(COLUMN_TITLE), "$COLUMN_TITLE = ?", arrayOf(title), null, null, null)
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun doesUpcomingFilmExist(title: String?): Boolean {
        if (title == null) return false
        val db = readableDatabase
        val cursor = db.query(TABLE_UPCOMING, arrayOf(COLUMN_TITLE), "$COLUMN_TITLE = ?", arrayOf(title), null, null, null)
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun deleteInvoice(invoice: Invoice) {
        val db = writableDatabase
        db.delete(
            TABLE_INVOICES,
            "$COLUMN_TITLE = ? AND $COLUMN_SHOW_DATE = ? AND $COLUMN_SHOW_TIME = ? AND $COLUMN_USER_EMAIL = ?",
            arrayOf(invoice.filmTitle, invoice.showDate, invoice.showTime, invoice.email)
        )
    }
}


