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
/**
 * Adapter для recyclerView с данными о сотрудниках
 * @author Денис
 * @since 1.0.0
 * @property[clickListener] интерфейс для обработки событий при нажатии на элемент списка.
 */
class EmployeesAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<EmployeeDTO, EmployeesAdapter.Holder>(COMPARATOR) {

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
        private val COMPARATOR = object: DiffUtil.ItemCallback<EmployeeDTO>(){

            /**
             * Компаратор для сравнения элементов списка сотрудников по их идентификаторам.
             *
             * Этот метод сравнивает уникальные идентификаторы двух объектов [EmployeeDTO],
             * чтобы определить, являются ли они одним и тем же элементом.
             *
             * @param oldItem Старый объект [EmployeeDTO] для сравнения.
             * @param newItem Новый объект [EmployeeDTO] для сравнения.
             * @return true, если у них одинаковые идентификаторы, в противном случае - false.
             */
            override fun areItemsTheSame(oldItem: EmployeeDTO, newItem: EmployeeDTO): Boolean {
                return oldItem.id == newItem.id
            }

            /**
             * Компаратор для сравнения содержимого двух объектов [EmployeeDTO].
             *
             * Этот метод сравнивает все поля двух объектов [EmployeeDTO], чтобы определить,
             * равны ли они друг другу по содержимому.
             *
             * @param oldItem Старый объект [EmployeeDTO] для сравнения.
             * @param newItem Новый объект [EmployeeDTO] для сравнения.
             * @return true, если содержимое объектов одинаково, в противном случае - false.
             */
            override fun areContentsTheSame(oldItem: EmployeeDTO, newItem: EmployeeDTO): Boolean {
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
        val employeeDTO = getItem(position) ?: return

        with(holder.largeItemRecyclerViewBinding){
            if(employeeDTO.patronymic.isEmpty()){
                mainTextView.text = employeeDTO.surname + " " + employeeDTO.name[0] + "."
            } else{
                mainTextView.text = employeeDTO.surname + " " + employeeDTO.name[0] + "." + employeeDTO.patronymic[0] + "."
            }

            subTextView.text = employeeDTO.post

            iconView.setImageBitmap(FileWorker().getBitmap(employeeDTO.photo))
        }
    }

    /**
     * Метод создания нового ViewHolder
     * @param[parent] контейнер, в который будет размещен новый ViewHolder
     * @param[viewType] тип ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewBinding.inflate(inflater, parent,false)

        return Holder(binding)
    }

    /**
     * Метод получения сотрудника
     * @param[position] позиция сотрудника в списке
     * @return данные о сотруднике
     */
    fun getEmployeeDTO(position: Int): EmployeeDTO?{
        return getItem(position)
    }

}