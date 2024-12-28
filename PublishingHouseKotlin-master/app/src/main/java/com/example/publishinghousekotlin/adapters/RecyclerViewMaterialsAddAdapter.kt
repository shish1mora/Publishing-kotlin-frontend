package com.example.publishinghousekotlin.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewWithAddBinding
import com.example.publishinghousekotlin.models.Material

/**
 * Adapter для recyclerView с данными о материалах для их добавления в продукцию
 * @author Денис
 * @since 1.0.0
 * @property[materials] список материлов
 */
class RecyclerViewMaterialsAddAdapter(private val materials: List<Material>) : RecyclerView.Adapter<RecyclerViewMaterialsAddAdapter.ViewHolder>()  {

    /**
     * Класс, предоставляющий элементы интерфейса, связанные с каждым элементом списка
     * @param[itemRecyclerViewWithAddBinding] binding для связи с ресурсами макета
     */
   inner class ViewHolder(val itemRecyclerViewWithAddBinding: ItemRecyclerViewWithAddBinding):RecyclerView.ViewHolder(itemRecyclerViewWithAddBinding.root)

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewMaterialsAddAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewWithAddBinding.inflate(inflater,parent,false)

        return ViewHolder(binding)
    }

    /**
     * Метод, связывающий данные из списка с элементом ViewHolder
     * @param[holder] элемент ViewHolder
     * @param[position] индекс элемента в списке
     */
    override fun onBindViewHolder(holder: RecyclerViewMaterialsAddAdapter.ViewHolder, position: Int) {
        val material = materials[position]

        with(holder.itemRecyclerViewWithAddBinding){
            iconView.setImageResource(R.drawable.material)
            mainTextView.text = material.type
            subTextView.text = "Стоимость за 1 шт: " + material.cost + " ₽"
        }
    }

    /**
     * Метод, возвращающий количество элементов в списке
     * @return Количество элементов в списке
     */
    override fun getItemCount(): Int {
        return materials.size
    }

    /**
     * Метод, возвращающий уникальный идентификатор элемента в указанной позиции
     * @return уникальный идентификатор элемента в указанной позиции
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Метод получения материала
     * @param[position] позиция материала в списке
     * @return данные о материале
     */
    fun getMaterial(position: Int):Material{
        return materials[position]
    }

}