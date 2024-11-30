package com.uilover.project2002.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uilover.project2002.data.model.Invoice
import com.uilover.project2002.databinding.ItemInvoiceBinding

class InvoiceListAdapter(
    private val invoiceList: List<Invoice>,
    private val onItemChecked: (Invoice, Boolean) -> Unit
) : RecyclerView.Adapter<InvoiceListAdapter.InvoiceViewHolder>() {

    private var isEditMode = false

    class InvoiceViewHolder(val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = invoiceList[position]
        holder.binding.filmName.text = invoice.filmTitle
        holder.binding.date.text = invoice.showDate
        holder.binding.checkBox.setOnCheckedChangeListener(null)
        holder.binding.checkBox.isChecked = invoice.isExpanded
        holder.binding.checkBox.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Thiết lập sự kiện click để mở rộng hoặc thu gọn
        holder.binding.root.setOnClickListener {
            invoice.isExpanded = !invoice.isExpanded
            notifyItemChanged(position)
        }

        // Thiết lập sự kiện click cho CheckBox
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onItemChecked(invoice, isChecked)
        }

        // Hiển thị thông tin chi tiết nếu mục đang mở rộng
        if (invoice.isExpanded) {
            holder.binding.detailsLayout.visibility = View.VISIBLE
            holder.binding.time.text = invoice.showTime
            holder.binding.seats.text = invoice.seats
            holder.binding.cinemaId.text = invoice.cinemaHall.toString()
            holder.binding.price.text = invoice.totalPrice.toString()
            holder.binding.barcode.text = invoice.barcode
        } else {
            holder.binding.detailsLayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return invoiceList.size
    }

    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged()
    }
}
