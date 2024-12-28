package com.example.publishinghousekotlin.fragments.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.adapters.UsersAdapter
import com.example.publishinghousekotlin.basics.OnItemClickListener
import com.example.publishinghousekotlin.controllers.DetailsCustomerActivity
import com.example.publishinghousekotlin.databinding.FragmentGeneralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerFragment: Fragment(), OnItemClickListener {

    private var _fragmentCustomersBinding: FragmentGeneralBinding? = null
    private val fragmentCustomersBinding get() = _fragmentCustomersBinding!!

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var adapter: UsersAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        customerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        _fragmentCustomersBinding = FragmentGeneralBinding.inflate(inflater, container, false)
        fragmentCustomersBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        fragmentCustomersBinding.searchTextInputLayout.hint = "Поиск по наименованию"

        adapter = UsersAdapter(this)
        fragmentCustomersBinding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //fragmentMaterialsBinding.recyclerView.setHasFixedSize(true)
        fragmentCustomersBinding.recyclerView.adapter = adapter

        customerViewModel.listOfCustomers.observe(this.viewLifecycleOwner){
            adapter.submitData(lifecycle, it)
        }

        fragmentCustomersBinding.searchEditText.addTextChangedListener {
            customerViewModel.updateSearchType(fragmentCustomersBinding.searchEditText.text.toString().trim())
        }


        return fragmentCustomersBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentCustomersBinding = null
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, DetailsCustomerActivity::class.java)
        intent.putExtra("customerId", adapter.getUser(position)?.id)
        startActivity(intent)
    }



}