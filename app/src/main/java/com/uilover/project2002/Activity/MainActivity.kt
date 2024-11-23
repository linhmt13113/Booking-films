package com.uilover.project2002.Activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.uilover.project2002.Adapter.SliderAdapter

import com.uilover.project2002.Models.SliderItems
import com.uilover.project2002.R
import com.uilover.project2002.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var textUserView: TextView

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("logged_in_email", null)

        if (loggedInUserEmail != null) {
            textView.text = loggedInUserEmail
            textUserView.text = "Hello ${loggedInUserEmail.split("@").firstOrNull()}"
        } else {
            textView.text = ""
            textUserView.text = "Hello User"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.textView4)
        textUserView = findViewById(R.id.textView3)

        val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val loggedInUserEmail = sharedPreferences.getString("logged_in_email", null)

        if (loggedInUserEmail != null) {
            textView.text = loggedInUserEmail
            textUserView.text = "Hello ${loggedInUserEmail.split("@").firstOrNull()}"
        } else {
            // Chuyển về màn hình đăng nhập nếu không có email
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cursor = dbHelper.getUser()

        if (cursor.moveToFirst()) {
            // Kiểm tra cột email có tồn tại không
            val emailColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)
            if (emailColumnIndex >= 0) {  // Kiểm tra xem cột có hợp lệ không
                val email = cursor.getString(emailColumnIndex)
                textView.text = email
                textUserView.text = "Hello ${email.split("@").firstOrNull()?.replaceFirstChar { it.uppercaseChar() } ?: "User"}"
            } else {
                // Nếu không tìm thấy cột email
                Toast.makeText(this, "Email column not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Nếu không có người dùng trong cơ sở dữ liệu, chuyển đến màn hình đăng nhập
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("logged_in_email")  // Xóa tất cả thông tin đăng nhập
            editor.apply()

            textView.text = ""
            textUserView.text = "Hello User"

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        insertBannerData()
        insertTopMoviesData()
        insertUpcomingMoviesData()

//        initBanner()
//        initTopMoving()
//        initUpcomming()

    }
    private fun insertBannerData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Banner Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Banner Description")
        }
        db.insert(DatabaseHelper.TABLE_BANNERS, null, values)
    }

    private fun insertTopMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Top Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Top Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_TOP_MOVIES, null, values)
    }

    private fun insertUpcomingMoviesData() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, "Upcoming Movie Title")
            put(DatabaseHelper.COLUMN_IMAGE_URL, "")
            put(DatabaseHelper.COLUMN_DESCRIPTION, "Upcoming Movie Description")
        }
        db.insert(DatabaseHelper.TABLE_UPCOMING, null, values)
    }



//    private fun initTopMoving() {
//        val dbHelper = DatabaseHelper(this)
//        val db = dbHelper.readableDatabase
//
//        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_TOP_MOVIES}", null)
//        val items = ArrayList<Film>()
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
//                val title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE))
//                val imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URL))
//                val description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))
//
//                val film = Film(id, title, imageUrl, description)
//                items.add(film)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//
//        binding.recyclerViewTopMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerViewTopMovies.adapter = FilmListAdapter(items)
//    }
//
//
//    private fun initUpcomming() {
//        val dbHelper = DatabaseHelper(this)
//        val db = dbHelper.readableDatabase
//
//        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_UPCOMING}", null)
//        val items = ArrayList<Film>()
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
//                val title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE))
//                val imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URL))
//                val description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))
//
//                val film = Film(id, title, imageUrl, description)
//                items.add(film)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//
//        binding.recyclerViewUpcomming.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerViewUpcomming.adapter = FilmListAdapter(items)
//    }
//
//
//    private fun initBanner() {
//        val dbHelper = DatabaseHelper(this)
//        val db = dbHelper.readableDatabase
//
//        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_BANNERS}", null)
//
//        val lists = mutableListOf<SliderItems>()
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                val id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
//                val title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE))
//                val imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URL))
//                val description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))
//
//                val sliderItem = SliderItems(id, title, imageUrl, description)
//                lists.add(sliderItem)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//
//        banners(lists)
//    }


    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(lists, binding.viewPager2)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close() // Đóng cơ sở dữ liệu
    }
}