package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.databinding.LargeItemRecyclerViewBinding
import com.example.publishinghousekotlin.dtos.EmployeeDTO

class DetailsRecyclerViewEmployeesAdapter(private val employees: List<EmployeeDTO>):RecyclerView.Adapter<DetailsRecyclerViewEmployeesAdapter.ViewHolder>() {

    inner class ViewHolder(val largeItemRecyclerViewBinding: LargeItemRecyclerViewBinding):RecyclerView.ViewHolder(largeItemRecyclerViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewBinding.inflate(inflater,parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employees[position]

        with(holder.largeItemRecyclerViewBinding){
            iconView.setImageBitmap(FileWorker().getBitmap(employee.photo))
            if(employee.patronymic.isEmpty()){
                mainTextView.text = employee.surname + " " + employee.name[0] + "."
            } else{
                mainTextView.text = employee.surname + " " + employee.name[0] + "." + employee.patronymic[0] + "."
            }

            subTextView.text = employee.post

        }
    }

}