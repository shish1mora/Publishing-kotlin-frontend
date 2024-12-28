package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.databinding.LargeItemRecyclerViewBinding
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.dtos.ProductAcceptDTO


/**
 * Adapter для recyclerView с данными о продукциях
 * @author Денис
 * @since 1.0.0
 * @property[clickListener] интерфейс для обработки событий при нажатии на элемент списка.
 */
class ProductsAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<ProductAcceptDTO, ProductsAdapter.Holder>(COMPARATOR) {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[largeItemRecyclerViewBinding] binding для связи с ресурсами макета
     */
    inner class Holder(val largeItemRecyclerViewBinding: LargeItemRecyclerViewBinding): RecyclerView.ViewHolder(largeItemRecyclerViewBinding.root), View.OnClickListener{

        init {
            largeItemRecyclerViewBinding.root.setOnClickListener(this)
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
        private val COMPARATOR = object: DiffUtil.ItemCallback<ProductAcceptDTO>(){

            /**
             * Компаратор для сравнения элементов списка продукций по их идентификаторам.
             *
             * Этот метод сравнивает уникальные идентификаторы двух объектов [ProductAcceptDTO],
             * чтобы определить, являются ли они одним и тем же элементом.
             *
             * @param oldItem Старый объект [ProductAcceptDTO] для сравнения.
             * @param newItem Новый объект [ProductAcceptDTO] для сравнения.
             * @return true, если у них одинаковые идентификаторы, в противном случае - false.
             */
            override fun areItemsTheSame(oldItem: ProductAcceptDTO, newItem: ProductAcceptDTO): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Компаратор для сравнения содержимого двух объектов [ProductAcceptDTO].
             *
             * Этот метод сравнивает все поля двух объектов [ProductAcceptDTO], чтобы определить,
             * равны ли они друг другу по содержимому.
             *
             * @param oldItem Старый объект [ProductAcceptDTO] для сравнения.
             * @param newItem Новый объект [ProductAcceptDTO] для сравнения.
             * @return true, если содержимое объектов одинаково, в противном случае - false.
             */
            override fun areContentsTheSame(oldItem: ProductAcceptDTO, newItem: ProductAcceptDTO): Boolean {
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
        val productDTO = getItem(position) ?: return

        with(holder.largeItemRecyclerViewBinding){
            mainTextView.text = productDTO.name
            subTextView.text = "Стоимость за 1 шт: " + productDTO.cost + " ₽"
            iconView.setImageBitmap(FileWorker().getBitmap(productDTO.photos!![0]))
        }
    }

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewBinding.inflate(inflater,parent,false)

        return Holder(binding)
    }

    /**
     * Метод получения продукции
     * @param[position] позиция продукции в списке
     * @return данные о продукции
     */
    fun getProductDTO(position: Int): ProductAcceptDTO?{
        return getItem(position)
    }
}