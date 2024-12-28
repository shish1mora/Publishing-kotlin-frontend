package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewTwoSubsBinding
import com.example.publishinghousekotlin.dtos.ProductMaterialDTO

/**
 * Adapter для recyclerView с данными о количестве материалов в продукции
 * @author Денис
 * @since 1.0.0
 * @property[materialsWithCount] список материалов и их количества в продукции
 */
class RecyclerViewWithCountMaterialsAdapter(private val materialsWithCount: List<ProductMaterialDTO>): RecyclerView.Adapter<RecyclerViewWithCountMaterialsAdapter.ViewHolder>() {

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
     * Метод, связывающий данные из списка с элементом ViewHolder
     * @param[holder] элемент ViewHolder
     * @param[position] индекс элемента в списке
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materialWithCount = materialsWithCount[position]

        with(holder.itemRecyclerViewTwoSubsBinding){
            iconView.setImageResource(R.drawable.material)
            mainTextView.text = materialWithCount.material.type
            firstSubTextView.text = "Стоимость за 1 шт: ${materialWithCount.material.cost} ₽"
            secondSubTextView.text = "Количество: ${materialWithCount.countMaterials} шт"
        }
    }

    /**
     * Метод, возвращающий количество элементов в списке
     * @return Количество элементов в списке
     */
    override fun getItemCount(): Int {
        return materialsWithCount.size
    }

    /**
     * Метод, возвращающий уникальный идентификатор элемента в указанной позиции
     * @return уникальный идентификатор элемента в указанной позиции
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}