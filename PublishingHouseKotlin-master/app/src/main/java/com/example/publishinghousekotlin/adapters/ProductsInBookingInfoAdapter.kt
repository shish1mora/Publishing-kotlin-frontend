package com.example.publishinghousekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.databinding.LargeItemRecyclerViewWithTwoSubsBinding
import com.example.publishinghousekotlin.dtos.ProductSimpleAcceptDTO

class ProductsInBookingInfoAdapter(private val products: List<ProductSimpleAcceptDTO>): RecyclerView.Adapter<ProductsInBookingInfoAdapter.ViewHolder>(){

    inner class ViewHolder(val largeItemRecyclerViewBinding: LargeItemRecyclerViewWithTwoSubsBinding): RecyclerView.ViewHolder(largeItemRecyclerViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LargeItemRecyclerViewWithTwoSubsBinding.inflate(inflater,parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        with(holder.largeItemRecyclerViewBinding){
            iconView.setImageBitmap(FileWorker().getBitmap(product.photo))
            mainTextView.text = product.name
            firstSubTextView.text = "Стоимость 1шт: ${product.cost}₽"
            secondSubTextView.text = "Количество: ${product.edition}"
        }
    }


}