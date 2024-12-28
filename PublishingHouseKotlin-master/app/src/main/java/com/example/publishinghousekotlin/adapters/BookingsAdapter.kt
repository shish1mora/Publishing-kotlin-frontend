package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewBinding
import com.example.publishinghousekotlin.dtos.BookingSimpleAcceptDTO


class BookingsAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<BookingSimpleAcceptDTO, BookingsAdapter.Holder>(COMPARATOR) {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[itemRecyclerViewBinding] binding для связи с ресурсами макета
     */
    inner class Holder(val itemRecyclerViewBinding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(itemRecyclerViewBinding.root), View.OnClickListener{
        init {
            itemRecyclerViewBinding.root.setOnClickListener(this)
        }

        /**
         * Обработка клика на элемент списка.
         *
         * @param[v] View, на котором был сделан клик.
         */
        override fun onClick(v: View?) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    companion object{
        private val COMPARATOR = object: DiffUtil.ItemCallback<BookingSimpleAcceptDTO>(){
            override fun areItemsTheSame(
                oldItem: BookingSimpleAcceptDTO,
                newItem: BookingSimpleAcceptDTO
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BookingSimpleAcceptDTO,
                newItem: BookingSimpleAcceptDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bookingSimpleAcceptDTO = getItem(position) ?: return

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.booking)
            mainTextView.text = "Заказ №${bookingSimpleAcceptDTO.id}"
            subTextView.text = "Стоимость: ${bookingSimpleAcceptDTO.cost} ₽"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)

        return Holder(binding)
    }


    fun getBookingSimpleAcceptDTO(position: Int): BookingSimpleAcceptDTO?{
        return getItem(position)
    }
}