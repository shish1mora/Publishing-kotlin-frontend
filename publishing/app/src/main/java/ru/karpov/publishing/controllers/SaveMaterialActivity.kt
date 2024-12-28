package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySaveMaterialBinding
import com.example.publishinghousekotlin.models.Material
import com.example.publishinghousekotlin.models.TypeProduct
import com.example.publishinghousekotlin.repositories.MaterialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal


/**
 * Activity для добавления/изменения материала
 *
 * @author Денис
 * @since 1.0.0
 */
class SaveMaterialActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var saveMaterialBinding: ActivitySaveMaterialBinding

    /**
     * Материал, который будет добавляться/изменяться
     */
    private var material: Material? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveMaterialBinding = ActivitySaveMaterialBinding.inflate(layoutInflater)
        setContentView(saveMaterialBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()
        setListeners()

        saveMaterialBinding.saveBtn.setOnClickListener {
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
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners() {
        val listener = Listener()

        listener.russianWordsListener(5,20,saveMaterialBinding.typeText, saveMaterialBinding.typeContainer)
        listener.sizeListener(saveMaterialBinding.sizeText, saveMaterialBinding.sizeContainer)
        listener.colorListener(saveMaterialBinding.colorText, saveMaterialBinding.colorContainer)
        listener.doubleNumberListener(0.1, 100.0, saveMaterialBinding.costText, saveMaterialBinding.costContainer)

    }

    /**
     * Метод установки стартовых данных
     */
    private fun setStartData() {
        material = intent.getSerializableExtra("material") as? Material

        if(material?.type != ""){
            saveMaterialBinding.typeText.setText(material!!.type)
            saveMaterialBinding.typeContainer.helperText = null
        }
        if(material?.size != ""){
            saveMaterialBinding.sizeText.setText(material!!.size)
            saveMaterialBinding.sizeContainer.helperText = null
        }
        if(material?.color != ""){
            saveMaterialBinding.colorText.setText(material!!.color)
            saveMaterialBinding.colorContainer.helperText = null
        }
        if(material?.cost != BigDecimal(0)){
            saveMaterialBinding.costText.setText(material!!.cost.toString())
            saveMaterialBinding.costContainer.helperText = null
        }
    }

    /**
     * Метод сохранения данных о материале
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun save(){
        val message = Messages()

        if(saveMaterialBinding.typeContainer.helperText == null && saveMaterialBinding.sizeContainer.helperText == null && saveMaterialBinding.colorContainer.helperText == null && saveMaterialBinding.costContainer.helperText == null){
            material!!.type = saveMaterialBinding.typeText.text.toString().trim()
            material!!.size = saveMaterialBinding.sizeText.text.toString()
            material!!.color = saveMaterialBinding.colorText.text.toString().trim()
            material!!.cost = saveMaterialBinding.costText.text.toString().trim().toBigDecimal()

            val materialRepository = MaterialRepository()
            if(material!!.id != 0.toLong()){
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        delay(1000)
                        withContext(Dispatchers.Main){
                            saveMaterialBinding.progressBar.visibility = View.VISIBLE
                        }

                        when(materialRepository.update(material!!)){
                            409 -> {
                                message.showError("В базе данных есть материал с введенными типом, цветом и размером", saveMaterialBinding.root)
                            }
                            400 -> {
                                message.showError("Данные о материале некорректны", saveMaterialBinding.root)
                            }
                            200 -> {
                                message.showSuccess("Данные о материале успешно изменены!", saveMaterialBinding.root)

                                delay(1000)
                                goToListMaterials()
                            }

                        }
                    }catch (e:Exception){
                        message.showError("Ошибка изменения данных о материале. Повторите попытку",saveMaterialBinding.root)
                    }
                    runOnUiThread {
                        saveMaterialBinding.progressBar.visibility = View.GONE
                    }
                }
            }else{
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            saveMaterialBinding.progressBar.visibility = View.VISIBLE
                        }

                        val responseCode = materialRepository.add(material!!)
                        if(responseCode == 409){
                            message.showError("В базе данных есть материал с введенными типом, цветом и размером", saveMaterialBinding.root)
                        }else if(responseCode == 200){
                            message.showSuccess("Материал успешно добавлен!", saveMaterialBinding.root)

                            delay(1000)
                            goToListMaterials()
                        }
                    }catch (e:Exception){
                        message.showError("Ошибка добавления материала. Повторите попытку",saveMaterialBinding.root)
                    }
                    runOnUiThread {
                        saveMaterialBinding.progressBar.visibility = View.GONE
                    }
                }
            }

        }else{
            message.showError("Некорректный ввод. Повторите попытку.",saveMaterialBinding.root)
        }
    }

    /**
     * Метод перехода к списку материалов
     */
    private fun goToListMaterials() {
        val intent = Intent(this@SaveMaterialActivity, MainActivity::class.java)
        intent.putExtra("fragment", "MaterialFragment")
        startActivity(intent)
    }

}