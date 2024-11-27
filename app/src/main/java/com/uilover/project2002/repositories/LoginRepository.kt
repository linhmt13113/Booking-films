package com.uilover.project2002.repositories

import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper

class LoginRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun checkUserLogin(email: String, password: String): Boolean {
        return dbHelper.checkUserLogin(email, password)
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
