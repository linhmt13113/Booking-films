package com.uilover.project2002.repositories

import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper

class RegisterRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun checkUserExists(email: String): Boolean {
        return dbHelper.checkUserExists(email)
    }

    fun insertUser(email: String, password: String) {
        dbHelper.insertUser(email, password)
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
