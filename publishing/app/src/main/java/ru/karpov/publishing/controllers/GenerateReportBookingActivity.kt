package com.example.publishinghousekotlin.controllers

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivityGenerateReportBinding
import com.example.publishinghousekotlin.http.requests.GenerateReportRequest
import com.example.publishinghousekotlin.http.responses.JwtResponse
import com.example.publishinghousekotlin.repositories.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class GenerateReportBookingActivity: AppCompatActivity() {

    private lateinit var activityGenerateReportBinding: ActivityGenerateReportBinding
    private var startPeriod = LocalDate.now()
    private var endPeriod = LocalDate.now()
    private val message = Messages()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityGenerateReportBinding = ActivityGenerateReportBinding.inflate(layoutInflater)
        setContentView(activityGenerateReportBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)


        activityGenerateReportBinding.startPeriodTextView.text = SpannableStringBuilder().bold { append("Начало периода: ") }.append(startPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        activityGenerateReportBinding.endPeriodTextView.text = SpannableStringBuilder().bold { append("Конец периода: ") }.append(endPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))

        setButtonActions()

        activityGenerateReportBinding.generateBtn.setOnClickListener {
            generateReport()
        }
    }

    private fun setButtonActions() {
        activityGenerateReportBinding.startPeriodBtn.setOnClickListener {
            val year = startPeriod.year
            val month = startPeriod.month.value - 1
            val day = startPeriod.dayOfMonth

            val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->

                startPeriod = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                activityGenerateReportBinding.startPeriodTextView.text = SpannableStringBuilder().bold { append("Начало периода: ") }.append(startPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            },year,month,day)

            datePickerDialog.show()
        }


        activityGenerateReportBinding.endPeriodBtn.setOnClickListener {
            val year = endPeriod.year
            val month = endPeriod.month.value - 1
            val day = endPeriod.dayOfMonth

            val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->

                endPeriod = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                activityGenerateReportBinding.endPeriodTextView.text = SpannableStringBuilder().bold { append("Конец периода: ") }.append(endPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            },year,month,day)

            datePickerDialog.show()
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


    private fun generateReport() {
        val period = Period.between(endPeriod, startPeriod)
        if(period.days > 0){
            message.showError("Дата начала периода должна быть меньше или равна дате конца периода", activityGenerateReportBinding.root)
        }else{

            val user = JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(
                R.string.keyForJwtResponse))!!.user
            val generateReportRequest = GenerateReportRequest(user.id, user.role, startPeriod, endPeriod)

            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    withContext(Dispatchers.Main) {
                        activityGenerateReportBinding.progressBar.visibility = View.VISIBLE
                    }
                    val messageResponse = BookingRepository().generateRepost(generateReportRequest)
                    if(messageResponse.code != 200){
                        message.showError(messageResponse.message, activityGenerateReportBinding.root)
                    }else{
                        message.showSuccess(messageResponse.message, activityGenerateReportBinding.root)
                    }


                }catch (e:Exception){
                    message.showError("Ошибка генерации отчёта", activityGenerateReportBinding.root)
                }

                runOnUiThread {
                    activityGenerateReportBinding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
}