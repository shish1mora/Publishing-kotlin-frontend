package com.example.publishinghousekotlin.controllers


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySaveTypeProductBinding
import com.example.publishinghousekotlin.models.TypeProduct
import com.example.publishinghousekotlin.repositories.TypeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity для добавления/изменения типа продукции
 *
 * @author Денис
 * @since 1.0.0
 */
class SaveTypeProductActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var saveTypeProductBinding: ActivitySaveTypeProductBinding

    /**
     * Тип продукции, который будет добавляться/изменяться
     */
    private var typeProduct: TypeProduct? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveTypeProductBinding = ActivitySaveTypeProductBinding.inflate(layoutInflater)
        setContentView(saveTypeProductBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()
        setListeners()


        saveTypeProductBinding.saveBtn.setOnClickListener {
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
    private fun setStartData(){
        typeProduct = intent.getSerializableExtra("typeProduct") as? TypeProduct

        if(typeProduct?.type != ""){
            saveTypeProductBinding.typeText.setText(typeProduct!!.type)
            saveTypeProductBinding.typeContainer.helperText = null
        }

        if(typeProduct?.margin != 0.0) {
            saveTypeProductBinding.marginText.setText(typeProduct!!.margin.toString())
            saveTypeProductBinding.marginContainer.helperText = null

        }
    }

    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners(){
        val listener = Listener()

        listener.russianWordsListener(5,20,saveTypeProductBinding.typeText, saveTypeProductBinding.typeContainer)
        listener.doubleNumberListener(1.00, 1000.00, saveTypeProductBinding.marginText, saveTypeProductBinding.marginContainer)
    }

    /**
     * Метод сохранения данных о типе продукции
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun save(){
        val message = Messages()

        if(saveTypeProductBinding.typeContainer.helperText == null && saveTypeProductBinding.marginContainer.helperText == null){
            typeProduct!!.type = saveTypeProductBinding.typeText.text.toString().trim()
            typeProduct!!.margin = saveTypeProductBinding.marginText.text.toString().trim().toDouble()


            val typeProductRepository = TypeProductRepository()
            if(typeProduct!!.id != 0.toLong()){
                lifecycleScope.launch(Dispatchers.IO) {
                    try{
                        delay(1000)
                        withContext(Dispatchers.Main){
                            saveTypeProductBinding.progressBar.visibility = View.VISIBLE
                        }

                        when (typeProductRepository.update(typeProduct!!)) {
                            409 -> {
                                message.showError("В базе данных уже существует тип продукции с наименованием ${typeProduct?.type}", saveTypeProductBinding.root)
                            }
                            400 -> {
                                message.showError("Данные о типе продукции некорректны", saveTypeProductBinding.root)
                            }
                            200 -> {
                                message.showSuccess("Данные о типе продукции успешно изменены!", saveTypeProductBinding.root)

                                delay(1000)
                                goToListTypeProducts()
                            }
                        }

                    }catch (e:Exception){
                        message.showError("Ошибка изменения данных о типе продукции. Повторите попытку",saveTypeProductBinding.root)
                    }
                    runOnUiThread {
                        saveTypeProductBinding.progressBar.visibility = View.GONE
                    }
                }
            }else{
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            saveTypeProductBinding.progressBar.visibility = View.VISIBLE
                        }

                        val responseCode = typeProductRepository.add(typeProduct!!)
                        if (responseCode == 409) {
                            message.showError("В базе данных уже существует тип продукции с наименованием ${typeProduct?.type}", saveTypeProductBinding.root)

                        } else if (responseCode == 200) {
                            message.showSuccess("Тип продукции успешно добавлен!", saveTypeProductBinding.root)

                            delay(1000)
                            goToListTypeProducts()

                        }
                    } catch (e:Exception){
                        message.showError("Ошибка добавления типа продукции. Повторите попытку",saveTypeProductBinding.root)
                    }
                    runOnUiThread {
                        saveTypeProductBinding.progressBar.visibility = View.GONE
                    }

                }
            }

        }else{
            message.showError("Некорректный ввод. Повторите попытку.",saveTypeProductBinding.root)
        }
    }

    /**
     * Метод перехода к списку типов продукции
     */
    private fun goToListTypeProducts(){

        val intent = Intent(this@SaveTypeProductActivity, MainActivity::class.java)
        intent.putExtra("fragment", "TypeProductFragment")
        startActivity(intent)
    }
}