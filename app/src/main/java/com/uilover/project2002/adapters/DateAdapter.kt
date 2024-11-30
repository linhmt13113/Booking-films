package com.uilover.project2002.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uilover.project2002.R
import com.uilover.project2002.databinding.ItemDateBinding

class DateAdapter(private val dates: List<String>, private val clickListener: (String) -> Unit) :
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class DateViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: String) {
            val dateParts = date.split("/")
            if (dateParts.size == 3) {
                binding.dayTxt.text = dateParts[0]
                binding.datMonthTxt.text = "${dateParts[1]} ${dateParts[2]}"

                if (selectedPosition == adapterPosition) {
                    binding.mailLayout.setBackgroundResource(R.drawable.white_bg)
                    binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                    binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                } else {
                    binding.mailLayout.setBackgroundResource(R.drawable.light_black_bg)
                    binding.dayTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    binding.datMonthTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }

                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        lastSelectedPosition = selectedPosition
                        selectedPosition = position
                        notifyItemChanged(lastSelectedPosition)
                        notifyItemChanged(selectedPosition)
                        clickListener(date)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount(): Int = dates.size
}
