package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.adapters.RecyclerViewProductsAddAdapter
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySaveBookingBinding
import com.example.publishinghousekotlin.dtos.BookingAcceptDTO
import com.example.publishinghousekotlin.dtos.BookingSendDTO
import com.example.publishinghousekotlin.dtos.ProductWithEditionDTO
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.repositories.BookingRepository
import com.example.publishinghousekotlin.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal


class SaveBookingActivity: AppCompatActivity() {

    private lateinit var saveBookingBinding: ActivitySaveBookingBinding

    private var message = Messages()

    private var recyclerViewProductsAddAdapter: RecyclerViewProductsAddAdapter? = null

    private var bookingSendDTO = BookingSendDTO()

    private val bookingRepository = BookingRepository()

    private var booking: BookingAcceptDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveBookingBinding = ActivitySaveBookingBinding.inflate(layoutInflater)
        setContentView(saveBookingBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        saveBookingBinding.saveBtn.setOnClickListener {
            save()
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

    private fun setStartData() {

        val bookingId = intent.getLongExtra("bookingId", 0)

        if(bookingId != 0L){
            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    booking = BookingRepository().get(bookingId)
                    bookingSendDTO.id = booking!!.id
                    withContext(Dispatchers.Main){
                        delay(100)
                        loadProducts()
                    }
                }catch (e:Exception){
                    message.showError("Ошибка получения данных о заказе", saveBookingBinding.root)
                }
            }
        }else{
            loadProducts()
        }

        //calculateCost()
    }

    private fun loadProducts(){
        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val products = ProductRepository().get(null, "")
                withContext(Dispatchers.Main){
                    if(products != null){
                        recyclerViewProductsAddAdapter = RecyclerViewProductsAddAdapter(products)
                        saveBookingBinding.productsRecyclerView.adapter = recyclerViewProductsAddAdapter
                        saveBookingBinding.productsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        saveBookingBinding.productsRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        saveBookingBinding.productsRecyclerView.post {
                            if(booking?.products != null){
                                for (i in 0 until recyclerViewProductsAddAdapter!!.itemCount) {
                                    val product = recyclerViewProductsAddAdapter!!.getProduct(i)

                                    val foundProduct = booking!!.products!!.find { it.id == product.id }
                                    if (foundProduct != null) {
                                        val viewHolder = saveBookingBinding.productsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewProductsAddAdapter.ViewHolder?

                                        viewHolder?.let {
                                            it.largeItemRecyclerViewWithAddBinding.countEditText.setText(foundProduct.edition.toString())
                                        }
                                    }
                                }
                            }
                        }


                        delay(100)
                        listenerOfCountProducts()
                        calculateCost()
                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка получения списка продукций", saveBookingBinding.root)
            }
        }
    }

    private fun listenerOfCountProducts(){
        for (i in 0 until recyclerViewProductsAddAdapter!!.itemCount) {
            val viewHolder = saveBookingBinding.productsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewProductsAddAdapter.ViewHolder?

            viewHolder?.let {
                val editText = it.largeItemRecyclerViewWithAddBinding.countEditText

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        calculateCost()
                    }

                    override fun afterTextChanged(editable: Editable?) {
                        editable?.let {
                            val enteredText = it.toString()
                            if (enteredText.isNotEmpty()) {
                                val countProducts = enteredText.toInt()

                                val minCountProducts = 1
                                val maxCountProducts = 1000

                                if (countProducts < minCountProducts) {
                                    editText.setText(minCountProducts.toString())
                                    message.showError("Минимальное количество продукции = $minCountProducts", saveBookingBinding.root)
                                } else if (countProducts > maxCountProducts) {
                                    editText.setText(maxCountProducts.toString())
                                    message.showError("Максимальное количество продукции = $maxCountProducts", saveBookingBinding.root)
                                }

                                if (enteredText.startsWith("0") && enteredText.length > 1) {
                                    it.replace(0, 1, "")
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    private fun calculateCost() {
        bookingSendDTO.cost = BigDecimal(0)

        for(i in 0 until recyclerViewProductsAddAdapter!!.itemCount){
            val viewHolder = saveBookingBinding.productsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewProductsAddAdapter.ViewHolder?

            viewHolder?.let {
                val countEditTextValue = it.largeItemRecyclerViewWithAddBinding.countEditText.text.toString()

                if(countEditTextValue.isNotEmpty()){
                    val selectedProduct = recyclerViewProductsAddAdapter!!.getProduct(i)
                    bookingSendDTO.cost = bookingSendDTO.cost.add(selectedProduct.cost.multiply(
                        BigDecimal(countEditTextValue.toInt())
                    ))
                }
            }
        }

        val costText = SpannableStringBuilder().bold { append("Итоговая стоимость: ") }.append(bookingSendDTO.cost.toString() + "₽")
        saveBookingBinding.costView.text = costText
    }

    private fun getProductsWithMargin(): MutableList<ProductWithEditionDTO>{
        var productsWithMargin: MutableList<ProductWithEditionDTO> = mutableListOf()

        for(i in 0 until recyclerViewProductsAddAdapter!!.itemCount){
            val viewHolder = saveBookingBinding.productsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewProductsAddAdapter.ViewHolder?

            viewHolder?.let {
                val countEditTextValue = it.largeItemRecyclerViewWithAddBinding.countEditText.text.toString()

                if(countEditTextValue.isNotEmpty()){
                    val product = recyclerViewProductsAddAdapter!!.getProduct(i)

                    val productWithMargin = ProductWithEditionDTO(product.id, null, null, countEditTextValue.toInt())
                    productsWithMargin.add(productWithMargin)

                }
            }
        }

        return productsWithMargin
    }

    private fun save(){
        val startIntervalCost = 0
        val endIntervalCost = 100000000

        if(bookingSendDTO.cost != BigDecimal(startIntervalCost) && bookingSendDTO.cost < BigDecimal(endIntervalCost)){
            bookingSendDTO.productsWithMargin = getProductsWithMargin()
            var messageResponse: MessageResponse?

            if(bookingSendDTO.id != 0.toLong()){
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        withContext(Dispatchers.Main) {
                            saveBookingBinding.progressBar.visibility = View.VISIBLE
                        }

                        bookingSendDTO.status = booking!!.status
                        bookingSendDTO.startExecution = booking!!.startExecution

                        messageResponse = bookingRepository.update(bookingSendDTO,"userUpdate")
                        if(messageResponse != null){
                            if(messageResponse!!.code != 200){
                                message.showError(messageResponse!!.message, saveBookingBinding.root)
                            }else{
                                message.showSuccess(messageResponse!!.message, saveBookingBinding.root)
                                delay(1000)
                                goToListBookings()
                            }
                        }

                    }catch (e:Exception){
                        message.showError("Ошибка изменения данных о заказе. Повторите попытку.",saveBookingBinding.root)
                    }

                    runOnUiThread {
                        saveBookingBinding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }else{
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        withContext(Dispatchers.Main) {
                            saveBookingBinding.progressBar.visibility = View.VISIBLE
                        }

                        messageResponse = bookingRepository.add(bookingSendDTO)
                        if(messageResponse != null){
                            if(messageResponse!!.code != 200){
                                message.showError(messageResponse!!.message, saveBookingBinding.root)
                            }else{
                                message.showSuccess(messageResponse!!.message, saveBookingBinding.root)
                                delay(1000)
                                goToListBookings()
                            }
                        }

                    }catch (e:Exception){
                        message.showError("Ошибка добавления заказа. Повторите попытку.", saveBookingBinding.root)
                    }

                    runOnUiThread {
                        saveBookingBinding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }else{
            message.showError("Стоимость заказа должна быть больше $startIntervalCost и меньше $endIntervalCost ₽", saveBookingBinding.root)
        }
    }

    /**
     * Метод перехода к списку заказов
     */
    private fun goToListBookings(){
        val intent = Intent(this@SaveBookingActivity, MainActivity::class.java)
        intent.putExtra("fragment", "BookingFragment")
        startActivity(intent)
    }
}