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
import com.example.publishinghousekotlin.models.User

class UsersAdapter(private val clickListener: OnItemClickListener): PagingDataAdapter<User, UsersAdapter.Holder> (COMPARATOR) {

    inner class Holder(val itemRecyclerViewBinding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(itemRecyclerViewBinding.root), View.OnClickListener{

        init {
            itemRecyclerViewBinding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    companion object{
        private val COMPARATOR = object : DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = getItem(position) ?: return

        with(holder.itemRecyclerViewBinding){
            iconView.setImageResource(R.drawable.customer)
            mainTextView.text = user.name
            subTextView.text = "Почта: ${user.email}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewBinding.inflate(inflater, parent, false)

        return Holder(binding)
    }

    fun getUser(position: Int): User?{
        return getItem(position)
    }
}