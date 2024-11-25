package com.uilover.project2002.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.uilover.project2002.activities.FilmDetailActivity
import com.uilover.project2002.data.model.Film
import com.uilover.project2002.databinding.ViewholderFilmBinding

class FilmListAdapter(private val items: ArrayList<Film>) : RecyclerView.Adapter<FilmListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.nameTxt.text = film.title
            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            Glide.with(itemView.context)
                .load(film.poster)
                .apply(requestOptions)
                .into(binding.pic)

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, FilmDetailActivity::class.java)
                intent.putExtra("object", film)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
