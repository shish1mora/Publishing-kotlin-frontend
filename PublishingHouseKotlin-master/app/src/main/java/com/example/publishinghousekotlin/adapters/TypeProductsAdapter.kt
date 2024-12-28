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


import com.example.publishinghousekotlin.models.TypeProduct

/**
 * Adapter для recyclerView с данными о типах продукции
 * @author Денис
 * @since 1.0.0
 * @property[clickListener] интерфейс для обработки событий при нажатии на элемент списка.
 */
class TypeProductsAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<TypeProduct, TypeProductsAdapter.Holder> (COMPARATOR) {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[itemRecyclerViewBinding] binding для связи с ресурсами макета
     */
    inner class Holder(val itemRecyclerViewBinding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(itemRecyclerViewBinding.root), View.OnClickListener {
        init{
            itemRecyclerViewBinding.root.setOnClickListener(this)
        }

        /**
         * Обработка клика на элемент списка.
         *
         * @param[v] View, на котором был сделан клик.
         */
        override fun onClick(view: View?) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    companion object{
        /**
         * Компаратор для сравнения элементов списка
         */
        private val COMPARATOR = object: DiffUtil.ItemCallback<TypeProduct>(){

            /**
             * Компаратор для сравнения элементов списка типов продукции по их идентификаторам.
             *
             * Этот метод сравнивает уникальные идентификаторы двух объектов [TypeProduct],
             * чтобы определить, являются ли они одним и тем же элементом.
             *
             * @param oldItem Старый объект [TypeProduct] для сравнения.
             * @param newItem Новый объект [TypeProduct] для сравнения.
             * @return true, если у них одинаковые идентификаторы, в противном случае - false.
             */
            override fun areItemsTheSame(oldItem: TypeProduct, newItem: TypeProduct): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Компаратор для сравнения содержимого двух объектов [TypeProduct].
             *
             * Этот метод сравнивает все поля двух объектов [TypeProduct], чтобы определить,
             * равны ли они друг другу по содержимому.
             *
             * @param oldItem Старый объект [TypeProduct] для сравнения.
             * @param newItem Новый объект [TypeProduct] для сравнения.
             * @return true, если содержимое объектов одинаково, в противном случае - false.
             */
            override fun areContentsTheSame(oldItem: TypeProduct, newItem: TypeProduct): Boolean {
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
        val typeProduct = getItem(position) ?: return

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.type_product)
//            if(typeProduct.type.length > 12){
//                mainTextView.text = typeProduct.type.substring(0,12) + "..."
//            }else {
//                mainTextView.text = typeProduct.type
//            }

            mainTextView.text = typeProduct.type
            subTextView.text = "Наценка: " + typeProduct.margin.toString() + "%"
        }
    }

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent,false)
        return Holder(binding)
    }

    /**
     * Метод получения типа продукции
     * @param[position] позиция типа продукции в списке
     * @return данные о типе продукции
     */
    fun getTypeProduct(position:Int):TypeProduct?{
        return getItem(position)
    }
}