package com.uilover.project2002.repositories

import android.content.Context
import com.uilover.project2002.data.local.DatabaseHelper
import org.mindrot.jbcrypt.BCrypt

class RegisterRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun checkUserExists(email: String): Boolean {
        return dbHelper.checkUserExists(email)
    }

    fun insertUser(username: String, email: String, password: String) {
        dbHelper.insertUser(username, email, password)
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
