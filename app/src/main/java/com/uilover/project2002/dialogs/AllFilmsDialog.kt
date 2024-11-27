package com.uilover.project2002.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.uilover.project2002.adapters.FilmListAdapter
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.databinding.DialogAllFilmsBinding

class AllFilmsDialog(context: Context, private val films: List<Film>) : Dialog(context) {
    private lateinit var binding: DialogAllFilmsBinding
    private lateinit var filmListAdapter: FilmListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAllFilmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT 
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams

        filmListAdapter = FilmListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = filmListAdapter

        filmListAdapter.submitList(films)

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }
}