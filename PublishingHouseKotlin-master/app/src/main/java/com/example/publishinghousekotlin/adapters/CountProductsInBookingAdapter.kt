package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewTwoSubsBinding
import com.example.publishinghousekotlin.dtos.CountProductsInBookingDTO

/**
 * Adapter для recyclerView с данными о количестве продукций в заказе
 * @author Денис
 * @since 1.0.0
 * @property[countProductsInBookingDTOS] список с продукциями и их количеством в заказе
 */
class CountProductsInBookingAdapter(private val countProductsInBookingDTOS: List<CountProductsInBookingDTO>): RecyclerView.Adapter<CountProductsInBookingAdapter.ViewHolder>() {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[itemRecyclerViewTwoSubsBinding] binding для связи с ресурсами макета
     */
    inner class ViewHolder(val itemRecyclerViewTwoSubsBinding: ItemRecyclerViewTwoSubsBinding): RecyclerView.ViewHolder(itemRecyclerViewTwoSubsBinding.root)

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewTwoSubsBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    /**
     * Метод, возвращающий количество элементов в списке
     * @return Количество элементов в списке
     */
    override fun getItemCount(): Int {
        return countProductsInBookingDTOS.size
    }

    /**
     * Метод, возвращающий уникальный идентификатор элемента в указанной позиции
     * @return уникальный идентификатор элемента в указанной позиции
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Метод, связывающий данные из списка с элементом ViewHolder
     * @param[holder] элемент ViewHolder
     * @param[position] индекс элемента в списке
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countProductsDTO = countProductsInBookingDTOS[position]

        with(holder.itemRecyclerViewTwoSubsBinding){
            iconView.setImageResource(R.drawable.booking)
            mainTextView.text = "Заказ №${countProductsDTO.booking.id}"
            firstSubTextView.text = "Стоимость выполнения: ${countProductsDTO.booking.cost}₽"
            secondSubTextView.text = "Количество продукции: ${countProductsDTO.edition}"

        }
    }


}