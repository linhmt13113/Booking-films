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
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        dbHelper.insertUser(username, email, hashedPassword)
    }

    fun closeDatabase() {
        dbHelper.close()
    }
}
