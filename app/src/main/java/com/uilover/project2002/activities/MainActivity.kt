package com.uilover.project2002.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uilover.project2002.adapters.FilmListAdapter
import com.uilover.project2002.adapters.SliderAdapter
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.data.model.SliderItems
import com.uilover.project2002.databinding.ActivityMainBinding
import com.uilover.project2002.dialogs.AllFilmsDialog
import com.uilover.project2002.viewmodels.MainViewModel
import com.uilover.project2002.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory(this) }
    private lateinit var filmListAdapter: FilmListAdapter
    private lateinit var sliderAdapter: SliderAdapter

    override fun onStart() {
        super.onStart()
        mainViewModel.updateUIWithEmail()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        mainViewModel.loggedInUserEmail.observe(this, { email ->
            if (email != null) {
                binding.textView4.text = email
                binding.textView3.text = "Hello ${email.split("@").firstOrNull()}"
            } else {
                navigateToLogin()
            }
        })

        binding.logout.setOnClickListener {
            mainViewModel.logout()
            navigateToIntro()
        }

        mainViewModel.insertInitialData()

        setupRecyclerView()
        setupSliderRecyclerView()
        observeViewModel()
        loadFilms()
        loadTopMovies()
    }

    private fun setupRecyclerView() {
        filmListAdapter = FilmListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = filmListAdapter
    }

    private fun setupSliderRecyclerView() {
        sliderAdapter = SliderAdapter(mutableListOf())
        binding.recyclerViewSlider.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewSlider.adapter = sliderAdapter
    }

    private fun observeViewModel() {
        mainViewModel.films.observe(this, { films ->
            if (films != null && films.isNotEmpty()) {
                Log.d("MainActivity", "Films loaded: ${films.size}")

                // Chỉ hiển thị 3 bộ phim đầu tiên
                val limitedFilms = films.take(3)
                filmListAdapter.submitList(limitedFilms)

                binding.progressBarSlider.visibility = View.GONE
            } else {
                binding.progressBarSlider.visibility = View.VISIBLE
                Log.d("MainActivity", "Films list is empty or null")
            }
        })

        mainViewModel.topMovies.observe(this, { topMovies ->
            // Hiển thị topMovies tại nơi bạn muốn trong giao diện
            // Ví dụ: bạn có thể thiết lập một RecyclerView khác để hiển thị topMovies
            Log.d("MainActivity", "Top Movies loaded: ${topMovies.size}")
        })

        // Xử lý sự kiện nhấn "See All"
        binding.textView6.setOnClickListener {
            // Mở một dialog hoặc activity mới để hiển thị tất cả các bộ phim
            showAllFilmsDialog(mainViewModel.films.value ?: emptyList())
        }
    }

    private fun showAllFilmsDialog(films: List<Film>) {
        val allFilmsDialog = AllFilmsDialog(this, films)
        allFilmsDialog.show()
    }

    private fun loadFilms() {
        mainViewModel.loadFilms()
    }

    private fun loadTopMovies() {
        mainViewModel.loadTopMovies()
    }

    private fun navigateToLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToIntro() {
        val intent = Intent(applicationContext, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.closeDatabase()
    }
}
