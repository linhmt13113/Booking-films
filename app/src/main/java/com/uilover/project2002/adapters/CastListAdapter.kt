package com.uilover.project2002.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uilover.project2002.data.model.Cast
import com.uilover.project2002.databinding.ViewholderCastBinding

class CastListAdapter(private val cast: ArrayList<Cast>) :
    RecyclerView.Adapter<CastListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewholderCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            Glide.with(itemView.context)
            binding.nameTxt.text = cast.actor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int {
        return cast.size
    }
}
