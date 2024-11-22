package com.uilover.project2002.Activity


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_BANNERS = "banners"
        const val TABLE_TOP_MOVIES = "top_movies"
        const val TABLE_UPCOMING = "upcoming"

        const val TABLE_USER = "user"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_EMAIL = "email"

        const val COLUMN_TITLE = "title"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_USER_PASSWORD = "password"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_USER ($COLUMN_USER_ID INTEGER PRIMARY KEY, $COLUMN_USER_EMAIL TEXT,$COLUMN_USER_PASSWORD TEXT)"
        val createBannersTable = "CREATE TABLE $TABLE_BANNERS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)"
        val createTopMoviesTable = "CREATE TABLE $TABLE_TOP_MOVIES ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)"
        val createUpcomingTable = "CREATE TABLE $TABLE_UPCOMING ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_IMAGE_URL TEXT, $COLUMN_DESCRIPTION TEXT)"

        db?.execSQL(createUserTable)
        db?.execSQL(createBannersTable)
        db?.execSQL(createTopMoviesTable)
        db?.execSQL(createUpcomingTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BANNERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TOP_MOVIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_UPCOMING")
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
}
