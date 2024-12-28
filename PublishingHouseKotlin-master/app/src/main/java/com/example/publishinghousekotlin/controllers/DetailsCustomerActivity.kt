package com.example.publishinghousekotlin.controllers

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.adapters.BookingsSimpleAcceptDTOAdapter
import com.example.publishinghousekotlin.adapters.ProductsInBookingInfoAdapter
import com.example.publishinghousekotlin.adapters.ProductsSimpleAcceptDTOAdapter
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.DetailsCustomerBinding
import com.example.publishinghousekotlin.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailsCustomerActivity: AppCompatActivity() {

    private lateinit var detailsCustomerBinding: DetailsCustomerBinding

    private var productsAreShown = false
    private var bookingsAreShown = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailsCustomerBinding = DetailsCustomerBinding.inflate(layoutInflater)
        setContentView(detailsCustomerBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        detailsCustomerBinding.productsRecyclerView.isVisible = false
        detailsCustomerBinding.bookingsRecyclerView.isVisible = false
        setStartData()

    }

    private fun setStartData() {
        var customerId = intent.getLongExtra("customerId", 0)
        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val customer = UserRepository().get(customerId)
                withContext(Dispatchers.Main){
                    if(customer != null){

                        // Текстовые данные
                        val name = SpannableStringBuilder().bold { append("Наименование: ") }.append(customer!!.name)
                        val email = SpannableStringBuilder().bold { append("Почта: ") }.append(customer!!.email)
                        val phone = SpannableStringBuilder().bold { append("Номер телефона: ") }.append(customer!!.phone)

                        detailsCustomerBinding.nameTextView.text = name
                        detailsCustomerBinding.emailTextView.text = email
                        detailsCustomerBinding.phoneTextView.text = phone


                        //Продукции
                        val productsAdapter = ProductsSimpleAcceptDTOAdapter(customer!!.products!!)
                        detailsCustomerBinding.productsRecyclerView.adapter = productsAdapter
                        detailsCustomerBinding.productsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        detailsCustomerBinding.productsRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        detailsCustomerBinding.showProductsBtn.setOnClickListener {
                            if (productsAreShown) {
                                productsAreShown = false
                                detailsCustomerBinding.productsRecyclerView.isVisible = false
                                detailsCustomerBinding.showProductsBtn.text = applicationContext.resources.getString(R.string.showProducts)
                            } else {
                                productsAreShown = true
                                detailsCustomerBinding.productsRecyclerView.isVisible = true
                                detailsCustomerBinding.showProductsBtn.text = applicationContext.resources.getString(R.string.hideProducts)
                            }
                        }

                        // Заказы
                        val bookingsAdapter = BookingsSimpleAcceptDTOAdapter(customer!!.bookings!!)
                        detailsCustomerBinding.bookingsRecyclerView.adapter = bookingsAdapter
                        detailsCustomerBinding.bookingsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        detailsCustomerBinding.bookingsRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        detailsCustomerBinding.showBookingsBtn.setOnClickListener {
                            if (bookingsAreShown) {
                                bookingsAreShown = false
                                detailsCustomerBinding.bookingsRecyclerView.isVisible = false
                                detailsCustomerBinding.showBookingsBtn.text = applicationContext.resources.getString(R.string.showBookings)
                            } else {
                                bookingsAreShown = true
                                detailsCustomerBinding.bookingsRecyclerView.isVisible = true
                                detailsCustomerBinding.showBookingsBtn.text = applicationContext.resources.getString(R.string.hideBookings)
                            }
                        }
                    }
                }
            }catch (e:Exception){
                Messages().showError("Ошибка получения данных о заказчике", detailsCustomerBinding.root)
            }
        }
    }

    /**
     * Переход в прошлую активность
     * @return завершать ли текущую активность
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}