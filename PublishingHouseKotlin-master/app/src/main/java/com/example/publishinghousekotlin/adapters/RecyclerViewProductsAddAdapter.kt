package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.databinding.ItemRecyclerViewWithAddBinding
import com.example.publishinghousekotlin.databinding.LargeItemRecyclerViewWithAddBinding
import com.example.publishinghousekotlin.dtos.ProductAcceptDTO

class RecyclerViewProductsAddAdapter(private val products: List<ProductAcceptDTO>): RecyclerView.Adapter<RecyclerViewProductsAddAdapter.ViewHolder>() {

    inner class ViewHolder(val largeItemRecyclerViewWithAddBinding: LargeItemRecyclerViewWithAddBinding): RecyclerView.ViewHolder(largeItemRecyclerViewWithAddBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewProductsAddAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewWithAddBinding.inflate(inflater,parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Метод, возвращающий уникальный идентификатор элемента в указанной позиции
     * @return уникальный идентификатор элемента в указанной позиции
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerViewProductsAddAdapter.ViewHolder, position: Int) {
       val product = products[position]

        val fileWorker = FileWorker()

        with(holder.largeItemRecyclerViewWithAddBinding){
            iconView.setImageBitmap(fileWorker.getBitmap(product.photos!![0]))
            mainTextView.text = product.name
            subTextView.text = "Стоимость за 1 шт: " + product.cost + " ₽"
        }
    }

    fun getProduct(position: Int):ProductAcceptDTO{
        return products[position]
    }


}