package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySavePrintingHouseBinding
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.PrintingHouse
import com.example.publishinghousekotlin.repositories.PrintingHouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity для добавления/изменения типографии
 *
 * @author Денис
 * @since 1.0.0
 */
class SavePrintingHouseActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var savePrintingHouseBinding: ActivitySavePrintingHouseBinding

    /**
     * Типография, которая будет добавляться/изменяться
     */
    private var printingHouse: PrintingHouse? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savePrintingHouseBinding = ActivitySavePrintingHouseBinding.inflate(layoutInflater)
        setContentView(savePrintingHouseBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()
        setListeners()

        savePrintingHouseBinding.saveBtn.setOnClickListener {
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

    /**
     * Метод установки стартовых данных
     */
    private fun setStartData() {
        printingHouse = intent.getSerializableExtra("printingHouse") as? PrintingHouse

        if(printingHouse?.name != ""){
            savePrintingHouseBinding.nameText.setText(printingHouse!!.name)
            savePrintingHouseBinding.nameContainer.helperText = null
        }
        if(printingHouse?.email != ""){
            savePrintingHouseBinding.emailText.setText(printingHouse!!.email)
            savePrintingHouseBinding.emailContainer.helperText = null
        }
        if(printingHouse?.phone != ""){
            savePrintingHouseBinding.phoneText.setText(printingHouse!!.phone)
            savePrintingHouseBinding.phoneContainer.helperText = null
        }
        if(printingHouse?.state != ""){
            savePrintingHouseBinding.stateText.setText(printingHouse!!.state)
            savePrintingHouseBinding.stateContainer.helperText = null
        }
        if(printingHouse?.city != ""){
            savePrintingHouseBinding.cityText.setText(printingHouse!!.city)
            savePrintingHouseBinding.cityContainer.helperText = null
        }
        if(printingHouse?.street != ""){
            savePrintingHouseBinding.streetText.setText(printingHouse!!.street)
            savePrintingHouseBinding.streetContainer.helperText = null
        }
        if(printingHouse?.house != ""){
            savePrintingHouseBinding.houseText.setText(printingHouse!!.house)
            savePrintingHouseBinding.houseContainer.helperText = null
        }

    }

    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners() {
        val listener = Listener()

        listener.nameListener(savePrintingHouseBinding.nameText, savePrintingHouseBinding.nameContainer)
        listener.phoneListener(savePrintingHouseBinding.phoneText, savePrintingHouseBinding.phoneContainer)
        listener.emailListener(savePrintingHouseBinding.emailText, savePrintingHouseBinding.emailContainer)
        listener.russianWordsListener(10,50,savePrintingHouseBinding.stateText, savePrintingHouseBinding.stateContainer)
        listener.cityListener(savePrintingHouseBinding.cityText, savePrintingHouseBinding.cityContainer)
        listener.streetListener(savePrintingHouseBinding.streetText, savePrintingHouseBinding.streetContainer)
        listener.houseNumberListener(savePrintingHouseBinding.houseText, savePrintingHouseBinding.houseContainer)

    }

    /**
     * Метод сохранения данных о типографии
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun save() {
        val message = Messages()

        if(savePrintingHouseBinding.nameContainer.helperText == null && savePrintingHouseBinding.emailContainer.helperText == null && savePrintingHouseBinding.phoneContainer.helperText == null &&
            savePrintingHouseBinding.stateContainer.helperText == null && savePrintingHouseBinding.cityContainer.helperText == null && savePrintingHouseBinding.streetContainer.helperText == null && savePrintingHouseBinding.houseContainer.helperText == null){
            printingHouse!!.name = savePrintingHouseBinding.nameText.text.toString().trim()
            printingHouse!!.email = savePrintingHouseBinding.emailText.text.toString().trim()
            printingHouse!!.phone = savePrintingHouseBinding.phoneText.text.toString()
            printingHouse!!.state = savePrintingHouseBinding.stateText.text.toString().trim()
            printingHouse!!.city = savePrintingHouseBinding.cityText.text.toString().trim()
            printingHouse!!.street = savePrintingHouseBinding.streetText.text.toString().trim()
            printingHouse!!.house = savePrintingHouseBinding.houseText.text.toString().trim()

            val printingHouseRepository = PrintingHouseRepository()
            var messageResponse: MessageResponse?
            if(printingHouse!!.id != 0.toLong()){
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        withContext(Dispatchers.Main){
                            savePrintingHouseBinding.progressBar.visibility = View.VISIBLE
                        }

                        messageResponse = printingHouseRepository.update(printingHouse!!)
                        if(messageResponse != null) {
                            if (messageResponse?.code != 200) {
                                message.showError(messageResponse!!.message, savePrintingHouseBinding.root)
                            }else{
                                message.showSuccess(messageResponse!!.message, savePrintingHouseBinding.root)

                                delay(1000)
                                goToListPrintingHouses()
                            }
                        }
                    }catch (e:Exception){
                        message.showError("Ошибка изменения данных о типографии. Повторите попытку",savePrintingHouseBinding.root)
                    }
                    runOnUiThread {
                        savePrintingHouseBinding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }else{
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            savePrintingHouseBinding.progressBar.visibility = View.VISIBLE
                        }

                        messageResponse = printingHouseRepository.add(printingHouse!!)
                        if(messageResponse != null){
                            if(messageResponse?.code == 409){
                                message.showError(messageResponse!!.message, savePrintingHouseBinding.root)
                            }else if(messageResponse?.code == 200){
                                message.showSuccess(messageResponse!!.message, savePrintingHouseBinding.root)

                                delay(1000)
                                goToListPrintingHouses()
                            }
                        }
                    }catch (e:Exception){
                        message.showError("Ошибка добавления типографии. Повторите попытку",savePrintingHouseBinding.root)
                    }
                    runOnUiThread {
                        savePrintingHouseBinding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }else{
            message.showError("Некорректный ввод. Повторите попытку.",savePrintingHouseBinding.root)
        }
    }

    /**
     * Метод перехода к списку типографий
     */
    private fun goToListPrintingHouses() {
        val intent = Intent(this@SavePrintingHouseActivity, MainActivity::class.java)
        intent.putExtra("fragment", "PrintingHouseFragment")
        startActivity(intent)
    }

}