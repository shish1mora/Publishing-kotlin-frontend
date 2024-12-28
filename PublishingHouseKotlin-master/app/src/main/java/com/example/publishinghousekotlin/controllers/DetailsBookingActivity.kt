package com.example.publishinghousekotlin.controllers


import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.adapters.DetailsRecyclerViewEmployeesAdapter
import com.example.publishinghousekotlin.adapters.ProductsInBookingInfoAdapter
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.DetailsBookingBinding
import com.example.publishinghousekotlin.dialogs.CompleteBookingDialog
import com.example.publishinghousekotlin.dialogs.DeleteBookingDialog
import com.example.publishinghousekotlin.dtos.BookingAcceptDTO
import com.example.publishinghousekotlin.http.responses.JwtResponse
import com.example.publishinghousekotlin.models.User
import com.example.publishinghousekotlin.models.UserRole
import com.example.publishinghousekotlin.repositories.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailsBookingActivity: AppCompatActivity() {

    private lateinit var detailsBookingBinding: DetailsBookingBinding
    private val message = Messages()
    private lateinit var user: User

    private var productsAreShown = false
    private var employeesAreShown = false

    private var booking: BookingAcceptDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailsBookingBinding = DetailsBookingBinding.inflate(layoutInflater)
        setContentView(detailsBookingBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        detailsBookingBinding.productsRecyclerView.isVisible = false
        detailsBookingBinding.employeesRecyclerView.isVisible = false

        user = JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(R.string.keyForJwtResponse))!!.user
        setStartData()

    }

    private fun setStartData() {
        var bookingId = intent.getLongExtra("bookingId", 0)
        lifecycleScope.launch(Dispatchers.IO) {
            try{
                booking = BookingRepository().get(bookingId)
                withContext(Dispatchers.Main){
                    if(booking != null){
                        // Текстовые данные
                        val id = SpannableStringBuilder().bold { append("Номер: ") }.append(booking!!.id.toString())
                        val status = SpannableStringBuilder().bold { append("Статус: ") }.append(booking!!.status)
                        val cost = SpannableStringBuilder().bold { append("Стоимость: ") }.append(booking!!.cost.toString() + " ₽")
                        val startExecution = SpannableStringBuilder().bold { append("Дата приёма: ") }.append(booking!!.startExecution.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        var endExecution = SpannableStringBuilder().bold { append("Дата выполнения: ") }.append("не установлена")
                        if(booking!!.endExecution != null) {
                            endExecution = SpannableStringBuilder().bold { append("Дата выполнения: ") }.append(booking!!.endExecution!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        }

                        var printingHouse = SpannableStringBuilder().bold { append("Типография: ") }.append("не установлена")
                        if(booking!!.printingHouse != null) {
                            printingHouse = SpannableStringBuilder().bold { append("Типография: ") }.append(booking!!.printingHouse!!.name)
                        }

                        detailsBookingBinding.idTextView.text = id
                        detailsBookingBinding.statusTextView.text = status
                        detailsBookingBinding.costTextView.text = cost
                        detailsBookingBinding.startExecutionTextView.text = startExecution
                        detailsBookingBinding.endExecutionView.text = endExecution
                        detailsBookingBinding.printingHouseView.text = printingHouse

                        if(user.role == UserRole.ADMINISTRATOR.name){
                            val customer = SpannableStringBuilder().bold { append("Заказчик: ") }.append(booking!!.products!![0].username)
                            detailsBookingBinding.customerView.text = customer

                            detailsBookingBinding.secondBtn.text = "Закончить выполнение"

                            when (booking!!.status) {
                                "ожидание" -> {
                                    detailsBookingBinding.firstBtn.isEnabled = false
                                    detailsBookingBinding.firstBtn.setBackgroundColor(0xFF6200EE.toInt())
                                    detailsBookingBinding.secondBtn.text = "Принять"

                                    detailsBookingBinding.secondBtn.setOnClickListener {

                                        val bookingAcceptDTO = BookingAcceptDTO(booking!!.id, booking!!.status, booking!!.startExecution, LocalDate.now(), booking!!.cost, null, null, null)

                                        val intent = Intent(this@DetailsBookingActivity, UpdateBookingAdminActivity::class.java)
                                        intent.putExtra("booking", bookingAcceptDTO)
                                        startActivity(intent)
                                    }
                                }
                                "выполняется" -> {
                                    detailsBookingBinding.firstBtn.isEnabled = true
                                    detailsBookingBinding.firstBtn.setOnClickListener {

                                        val bookingAcceptDTO = BookingAcceptDTO(booking!!.id, booking!!.status, booking!!.startExecution, booking!!.endExecution, booking!!.cost, booking!!.printingHouse, null, booking!!.employees)

                                        val intent = Intent(this@DetailsBookingActivity, UpdateBookingAdminActivity::class.java)
                                        intent.putExtra("booking", bookingAcceptDTO)
                                        startActivity(intent)
                                    }

                                    detailsBookingBinding.secondBtn.setOnClickListener {
                                        CompleteBookingDialog(booking!!.id, detailsBookingBinding.root).show(supportFragmentManager, "COMPLETEBOOKINGDIALOG")
                                    }
                                }
                                else -> {
                                    detailsBookingBinding.firstBtn.isEnabled = false
                                    detailsBookingBinding.firstBtn.setBackgroundColor(0xFF6200EE.toInt())
                                    detailsBookingBinding.secondBtn.isEnabled = false
                                }
                            }
                        }else{
                            if(booking!!.status != "ожидание"){
                                detailsBookingBinding.firstBtn.isEnabled = false
                                detailsBookingBinding.firstBtn.setBackgroundColor(0xFF6200EE.toInt())
                                detailsBookingBinding.secondBtn.isEnabled = false
                            }else{

                                detailsBookingBinding.firstBtn.setOnClickListener {
                                    val intent = Intent(this@DetailsBookingActivity, SaveBookingActivity::class.java)
                                    intent.putExtra("bookingId", booking!!.id)
                                    startActivity(intent)
                                }

                                detailsBookingBinding.secondBtn.setOnClickListener {
                                    DeleteBookingDialog(booking!!.id, detailsBookingBinding.root).show(supportFragmentManager, "DELETEBOOKINGDIALOG")
                                }
                            }
                        }


                        // Загрузка продукции
                        val productsAdapter = ProductsInBookingInfoAdapter(booking!!.products!!)
                        detailsBookingBinding.productsRecyclerView.adapter = productsAdapter
                        detailsBookingBinding.productsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        detailsBookingBinding.productsRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        detailsBookingBinding.showProductsBtn.setOnClickListener {
                            if (productsAreShown) {
                                productsAreShown = false
                                detailsBookingBinding.productsRecyclerView.isVisible = false
                                detailsBookingBinding.showProductsBtn.text = applicationContext.resources.getString(R.string.showProducts)
                            } else {
                                productsAreShown = true
                                detailsBookingBinding.productsRecyclerView.isVisible = true
                                detailsBookingBinding.showProductsBtn.text = applicationContext.resources.getString(R.string.hideProducts)
                            }
                        }

                        // Загрузка сотрудников
                        val employeesAdapter = DetailsRecyclerViewEmployeesAdapter(booking!!.employees!!)
                        detailsBookingBinding.employeesRecyclerView.adapter = employeesAdapter
                        detailsBookingBinding.employeesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        detailsBookingBinding.employeesRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        detailsBookingBinding.showEmployeesBtn.setOnClickListener {
                            if (employeesAreShown) {
                                employeesAreShown = false
                                detailsBookingBinding.employeesRecyclerView.isVisible = false
                                detailsBookingBinding.showEmployeesBtn.text = applicationContext.resources.getString(R.string.showEmployees)
                            } else {
                                employeesAreShown = true
                                detailsBookingBinding.employeesRecyclerView.isVisible = true
                                detailsBookingBinding.showEmployeesBtn.text = applicationContext.resources.getString(R.string.hideEmployees)
                            }
                        }

                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка загрузки данных о заказе", detailsBookingBinding.root)
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