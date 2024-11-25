package com.uilover.project2002.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.uilover.project2002.data.model.SliderItems
import com.uilover.project2002.databinding.ViewholderSliderBinding

class SliderAdapter(
    private var sliderItems: MutableList<SliderItems>, private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(private val binding: ViewholderSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: SliderItems) {
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))
            Glide.with(itemView.context)
                .load(sliderItem.image)
                .apply(requestOptions)
                .into(binding.imageSlide)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderViewHolder {
        val binding = ViewholderSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size
}
