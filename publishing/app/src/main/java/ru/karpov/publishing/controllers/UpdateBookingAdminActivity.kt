package com.example.publishinghousekotlin.controllers

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.adapters.EmployeesSelectAdapter
import com.example.publishinghousekotlin.adapters.PrintingHousesSpinnerAdapter
import com.example.publishinghousekotlin.adapters.RecyclerViewMaterialsAddAdapter
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.UpdateBookingAdminBinding
import com.example.publishinghousekotlin.dtos.BookingAcceptDTO
import com.example.publishinghousekotlin.dtos.BookingSendDTO
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.repositories.BookingRepository
import com.example.publishinghousekotlin.repositories.EmployeeRepository
import com.example.publishinghousekotlin.repositories.PrintingHouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period

class UpdateBookingAdminActivity: AppCompatActivity() {

    private lateinit var updateBookingAdminBinding: UpdateBookingAdminBinding
    private val message = Messages()
    private var employeesSelectAdapter: EmployeesSelectAdapter? = null
    private var bookingSendDTO = BookingSendDTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBookingAdminBinding = UpdateBookingAdminBinding.inflate(layoutInflater)
        setContentView(updateBookingAdminBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        updateBookingAdminBinding.saveBtn.setOnClickListener {
            save()
        }
    }

    private fun setStartData() {
        val bookingAcceptDTO = intent.getSerializableExtra("booking") as? BookingAcceptDTO

        bookingSendDTO.id = bookingAcceptDTO!!.id
        bookingSendDTO.status = bookingAcceptDTO!!.status
        bookingSendDTO.startExecution = bookingAcceptDTO!!.startExecution
        bookingSendDTO.endExecution = bookingAcceptDTO!!.endExecution
        bookingSendDTO.cost = bookingAcceptDTO!!.cost
        bookingSendDTO.printingHouse = bookingAcceptDTO!!.printingHouse

//        if(bookingAcceptDTO!!.employees != null){
//            val employeesId: MutableList<Long> = mutableListOf()
//            for(i in 0 until bookingAcceptDTO!!.employees!!.size){
//                employeesId.add(bookingAcceptDTO!!.employees!![i].id)
//            }
//
//            bookingSendDTO.idsOfEmployees = employeesId
//        }

        message.setSuccess(updateBookingAdminBinding.endExecutionBtnHelperText, "Дата выполнения сохранена")

        setEndExecution()
        loadPrintingHouses()
        loadEmployees(bookingAcceptDTO!!.employees)
    }

    private fun setEndExecution(){
        updateBookingAdminBinding.chooseEndExecutionBtn.setOnClickListener {

            val year = bookingSendDTO.endExecution!!.year
            val month = bookingSendDTO.endExecution!!.month.value - 1
            val day = bookingSendDTO.endExecution!!.dayOfMonth

            val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->

                bookingSendDTO.endExecution = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                val period = Period.between(LocalDate.now(), bookingSendDTO.endExecution)
                if(period.days < 0){
                    message.setError(updateBookingAdminBinding.endExecutionBtnHelperText, "Дата выполнения не может быть в прошлом")
                }else {
                    message.setSuccess(updateBookingAdminBinding.endExecutionBtnHelperText, "Дата выполнения выбрана")
                }
            },year,month,day)

            datePickerDialog.show()

        }
    }


    private fun loadPrintingHouses() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val printingHouses = PrintingHouseRepository().get(null, "")
                withContext(Dispatchers.Main) {
                    if (printingHouses != null) {
                        val spinnerAdapter = PrintingHousesSpinnerAdapter(applicationContext, printingHouses)
                        updateBookingAdminBinding.printingHousesSpinner.adapter = spinnerAdapter

                        if(bookingSendDTO!!.printingHouse != null){
                            val position = spinnerAdapter.getPosition(bookingSendDTO!!.printingHouse)
                            if(position != Spinner.INVALID_POSITION){
                                updateBookingAdminBinding.printingHousesSpinner.setSelection(position)
                            }
                        }

                        updateBookingAdminBinding.printingHousesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                bookingSendDTO.printingHouse = spinnerAdapter.getSelectedPrintingHouse(position)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка получения типографий", updateBookingAdminBinding.root)
            }
        }
    }

    private fun loadEmployees(employeeDTOS: List<EmployeeDTO>?) {
        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val employees = EmployeeRepository().get(null, "")
                withContext(Dispatchers.Main){
                    if(employees != null){
                        employeesSelectAdapter = EmployeesSelectAdapter(employees)
                        updateBookingAdminBinding.employeesRecyclerView.adapter = employeesSelectAdapter
                        updateBookingAdminBinding.employeesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        updateBookingAdminBinding.employeesRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                    }

                    updateBookingAdminBinding.employeesRecyclerView.post {
                        if (employeeDTOS != null) {
                            for (i in 0 until employeesSelectAdapter!!.itemCount) {
                                val employee = employeesSelectAdapter!!.getEmployee(i)

                                val foundEmployee = employeeDTOS!!.find { it.id == employee.id }
                                if (foundEmployee != null) {
                                    val viewHolder = updateBookingAdminBinding.employeesRecyclerView.findViewHolderForAdapterPosition(i) as EmployeesSelectAdapter.ViewHolder?

                                    viewHolder?.let {
                                        it.largeItemRecyclerViewWithSelectBinding.selectEmployeeBox.isChecked = true
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка получения сотрудников", updateBookingAdminBinding.root)
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

    private fun getSelectedEmployees(): MutableList<Long>{
        var idsOfEmployees: MutableList<Long> = mutableListOf()
        for(i in 0 until employeesSelectAdapter!!.itemCount){
            val viewHolder = updateBookingAdminBinding.employeesRecyclerView.findViewHolderForAdapterPosition(i) as EmployeesSelectAdapter.ViewHolder?

            viewHolder?.let {

                if(it.largeItemRecyclerViewWithSelectBinding.selectEmployeeBox.isChecked){
                    val employeeId = employeesSelectAdapter!!.getEmployee(i).id
                    idsOfEmployees.add(employeeId)

                }
            }
        }

        return idsOfEmployees
    }

    private fun save() {
        bookingSendDTO.idsOfEmployees = getSelectedEmployees()
        if((bookingSendDTO.idsOfEmployees as MutableList<Long>).isNotEmpty() && !updateBookingAdminBinding.endExecutionBtnHelperText.text.toString().contains("не может быть в прошлом")){
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        updateBookingAdminBinding.progressBar.visibility = View.VISIBLE
                    }

                    val messageResponse = BookingRepository().update(bookingSendDTO, "execute")
                    if (messageResponse != null) {
                        if (messageResponse!!.code != 200) {
                            message.showError(messageResponse!!.message, updateBookingAdminBinding.root)
                        } else {
                            message.showSuccess(messageResponse!!.message, updateBookingAdminBinding.root)
                            delay(1000)


                            val intent = Intent(this@UpdateBookingAdminActivity, MainActivity::class.java)
                            intent.putExtra("fragment", "BookingFragment")
                            startActivity(intent)

                        }
                    }
                }catch (e:Exception){
                    message.showError("Ошибка обновления заказа. Повторите попытку", updateBookingAdminBinding.root)
                }

                runOnUiThread {
                    updateBookingAdminBinding.progressBar.visibility = View.INVISIBLE
                }
            }
        }else{
            message.showError("Некорректный ввод. Повторите попытку", updateBookingAdminBinding.root)
        }
    }


}