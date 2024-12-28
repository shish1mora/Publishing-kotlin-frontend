package com.example.publishinghousekotlin.controllers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.example.publishinghousekotlin.basics.MapWorker
import com.example.publishinghousekotlin.databinding.DetailsPrintingHouseBinding
import com.example.publishinghousekotlin.dialogs.DeleteMaterialDialog
import com.example.publishinghousekotlin.dialogs.DeletePrintingHouseDialog
import com.example.publishinghousekotlin.models.PrintingHouse


/**
 * Activity для вывода подробной информации о материале
 *
 * @author Денис
 * @since 1.0.0
 */
class DetailsPrintingHouseActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var detailsPrintingHouseBinding: DetailsPrintingHouseBinding

    /**
     * Типография, данные о которой будут выводиться
     */
    private var printingHouse: PrintingHouse? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailsPrintingHouseBinding = DetailsPrintingHouseBinding.inflate(layoutInflater)
        setContentView(detailsPrintingHouseBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        detailsPrintingHouseBinding.deleteBtn.setOnClickListener {
            DeletePrintingHouseDialog(printingHouse!!.id, detailsPrintingHouseBinding.root).show(supportFragmentManager,"DELETEPRINTINGHOUSEDIALOG")
        }

        detailsPrintingHouseBinding.editBtn.setOnClickListener {
            val intent = Intent(this@DetailsPrintingHouseActivity, SavePrintingHouseActivity::class.java)
            intent.putExtra("printingHouse", printingHouse)
            startActivity(intent)
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

    /**
     * Метод установки стартовых данных
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setStartData() {
        printingHouse = intent.getSerializableExtra("printingHouse") as? PrintingHouse

        val name = SpannableStringBuilder().bold { append("Наименование: ") }.append(printingHouse?.name)
        val state = SpannableStringBuilder().bold { append("Субъект: ") }.append(printingHouse?.state)
        val city = SpannableStringBuilder().bold { append("Город: ") }.append(printingHouse?.city)
        val street = SpannableStringBuilder().bold { append("Улица: ") }.append(printingHouse?.street)
        val house = SpannableStringBuilder().bold { append("Номер дома: ") }.append(printingHouse?.house)
        val email = SpannableStringBuilder().bold { append("Электронная почта: ") }.append(printingHouse?.email)
        val phone = SpannableStringBuilder().bold { append("Номер телефона: ") }.append(printingHouse?.phone)

        detailsPrintingHouseBinding.nameTextView.text = name
        detailsPrintingHouseBinding.stateTextView.text = state
        detailsPrintingHouseBinding.cityTextView.text = city
        detailsPrintingHouseBinding.streetTextView.text = street
        detailsPrintingHouseBinding.houseTextView.text = house
        detailsPrintingHouseBinding.emailTextView.text = email
        detailsPrintingHouseBinding.phoneTextView.text = phone


        val address = "$city, $street, $house"
        MapWorker().showAddress(address, detailsPrintingHouseBinding.mapView)
    }

}