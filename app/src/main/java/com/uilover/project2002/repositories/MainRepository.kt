package com.uilover.project2002.repositories

import android.content.ContentValues
import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper



class MainRepository(val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertBannerData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Banner Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Banner Description")
        }
        db.insert(DatabaseHelper.TABLE_BANNERS, null, values)
    }

    fun insertTopMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Top Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Top Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_TOP_MOVIES, null, values)
    }

    fun insertUpcomingMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Upcoming Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Upcoming Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_UPCOMING, null, values)
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
