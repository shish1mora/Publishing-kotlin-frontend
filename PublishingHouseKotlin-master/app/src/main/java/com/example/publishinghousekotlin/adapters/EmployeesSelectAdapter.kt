package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.databinding.LargeItemRecyclerViewWithSelectBinding
import com.example.publishinghousekotlin.dtos.EmployeeDTO

class EmployeesSelectAdapter(private val employees: List<EmployeeDTO>): RecyclerView.Adapter<EmployeesSelectAdapter.ViewHolder>() {

    inner class ViewHolder(val largeItemRecyclerViewWithSelectBinding: LargeItemRecyclerViewWithSelectBinding): RecyclerView.ViewHolder(largeItemRecyclerViewWithSelectBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewWithSelectBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    /**
     * Метод, возвращающий уникальный идентификатор элемента в указанной позиции
     * @return уникальный идентификатор элемента в указанной позиции
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employees[position]

        with(holder.largeItemRecyclerViewWithSelectBinding){
            iconView.setImageBitmap(FileWorker().getBitmap(employee.photo))
            if(employee.patronymic.isEmpty()){
                mainTextView.text = employee.surname + " " + employee.name[0] + "."
            } else{
                mainTextView.text = employee.surname + " " + employee.name[0] + "." + employee.patronymic[0] + "."
            }

            subTextView.text = employee.post
        }
    }

    fun getEmployee(position: Int):EmployeeDTO{
        return employees[position]
    }

}