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
import com.example.publishinghousekotlin.models.Material

/**
 * Adapter для recyclerView с данными о материалах
 * @author Денис
 * @since 1.0.0
 * @property[clickListener] интерфейс для обработки событий при нажатии на элемент списка.
 */
class MaterialsAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<Material, MaterialsAdapter.Holder> (COMPARATOR) {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[itemRecyclerViewBinding] binding для связи с ресурсами макета
     */
    inner class Holder(val itemRecyclerViewBinding: ItemRecyclerViewBinding):RecyclerView.ViewHolder(itemRecyclerViewBinding.root), View.OnClickListener{
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
        private val COMPARATOR = object: DiffUtil.ItemCallback<Material>(){

            /**
             * Компаратор для сравнения элементов списка материалов по их идентификаторам.
             *
             * Этот метод сравнивает уникальные идентификаторы двух объектов [Material],
             * чтобы определить, являются ли они одним и тем же элементом.
             *
             * @param oldItem Старый объект [Material] для сравнения.
             * @param newItem Новый объект [Material] для сравнения.
             * @return true, если у них одинаковые идентификаторы, в противном случае - false.
             */
            override fun areItemsTheSame(oldItem: Material, newItem: Material): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Компаратор для сравнения содержимого двух объектов [Material].
             *
             * Этот метод сравнивает все поля двух объектов [Material], чтобы определить,
             * равны ли они друг другу по содержимому.
             *
             * @param oldItem Старый объект [Material] для сравнения.
             * @param newItem Новый объект [Material] для сравнения.
             * @return true, если содержимое объектов одинаково, в противном случае - false.
             */

            override fun areContentsTheSame(oldItem: Material, newItem: Material): Boolean {
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
        val material = getItem(position) ?: return

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.material)
            mainTextView.text = material.type
            subTextView.text = "Стоимость за 1 шт: " + material.cost + " ₽"
        }
    }

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater,parent,false)

        return Holder(binding)
    }

    /**
     * Метод получения материала
     * @param[position] позиция материала в списке
     * @return данные о материале
     */
    fun getMaterial(position: Int): Material?{
        return getItem(position)
    }

}
