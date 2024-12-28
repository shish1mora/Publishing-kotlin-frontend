package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewBinding
import com.example.publishinghousekotlin.dtos.BookingSimpleAcceptDTO

class BookingsSimpleAcceptDTOAdapter(private val bookings: List<BookingSimpleAcceptDTO>): RecyclerView.Adapter<BookingsSimpleAcceptDTOAdapter.ViewHolder>() {

    inner class ViewHolder(val itemRecyclerViewBinding: ItemRecyclerViewBinding):RecyclerView.ViewHolder(itemRecyclerViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater,parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.booking)
            mainTextView.text = "Заказ №${booking.id}"
            subTextView.text = "Стоимость: ${booking.cost} ₽"

        }
    }

}