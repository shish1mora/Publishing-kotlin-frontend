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
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.models.PrintingHouse

/**
 * Adapter для recyclerView с данными о типографиях
 * @author Денис
 * @since 1.0.0
 * @property[clickListener] интерфейс для обработки событий при нажатии на элемент списка.
 */
class PrintingHouseAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<PrintingHouse, PrintingHouseAdapter.Holder>(COMPARATOR) {

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
        /**
         * Компаратор для сравнения элементов списка
         */
        private val COMPARATOR = object: DiffUtil.ItemCallback<PrintingHouse>(){

            /**
             * Компаратор для сравнения элементов списка типографий по их идентификаторам.
             *
             * Этот метод сравнивает уникальные идентификаторы двух объектов [PrintingHouse],
             * чтобы определить, являются ли они одним и тем же элементом.
             *
             * @param oldItem Старый объект [PrintingHouse] для сравнения.
             * @param newItem Новый объект [PrintingHouse] для сравнения.
             * @return true, если у них одинаковые идентификаторы, в противном случае - false.
             */
            override fun areItemsTheSame(oldItem: PrintingHouse, newItem: PrintingHouse): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Компаратор для сравнения содержимого двух объектов [PrintingHouse].
             *
             * Этот метод сравнивает все поля двух объектов [PrintingHouse], чтобы определить,
             * равны ли они друг другу по содержимому.
             *
             * @param oldItem Старый объект [PrintingHouse] для сравнения.
             * @param newItem Новый объект [PrintingHouse] для сравнения.
             * @return true, если содержимое объектов одинаково, в противном случае - false.
             */
            override fun areContentsTheSame(oldItem: PrintingHouse, newItem: PrintingHouse): Boolean {
                return oldItem == newItem
            }

        }
    }

    /**
     * Метод, связывающий данные из списка с элементом ViewHolder
     * @param[holder] элемент ViewHolder
     * @param[position] индекс элемента в списке
     */
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val printingHouse = getItem(position) ?: return

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.printing_house)
            mainTextView.text = printingHouse.name
            subTextView.text = printingHouse.email
        }
    }

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)

        return Holder(binding)
    }

    /**
     * Метод получения типографии
     * @param[position] позиция типографии в списке
     * @return данные о типографии
     */
    fun getPrintingHouse(position: Int): PrintingHouse?{
        return getItem(position)
    }
}