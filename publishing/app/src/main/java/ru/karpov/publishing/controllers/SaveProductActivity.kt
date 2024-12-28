package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.adapters.RecyclerViewMaterialsAddAdapter
import com.example.publishinghousekotlin.adapters.TypeProductsSpinnerAdapter
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySaveProductBinding
import com.example.publishinghousekotlin.dtos.ProductAcceptDTO
import com.example.publishinghousekotlin.dtos.ProductSendDTO
import com.example.publishinghousekotlin.dtos.ProductMaterialDTO
import com.example.publishinghousekotlin.http.responses.JwtResponse
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.repositories.MaterialRepository
import com.example.publishinghousekotlin.repositories.ProductRepository
import com.example.publishinghousekotlin.repositories.TypeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Activity для добавления/изменения продукции
 *
 * @author Денис
 * @since 1.0.0
 */
class SaveProductActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var saveProductBinding: ActivitySaveProductBinding

    /**
     * Адаптер recyclerView для списка материалов
     */
    private var recyclerViewMaterialsAdapter: RecyclerViewMaterialsAddAdapter? = null

    /**
     * Экземпляр класса для отображения сообщений пользователю
     */
    private var message = Messages()

    /**
     * Репозиторий для получения/отправки данных на сервер
     */
    private val productRepository = ProductRepository()

    /**
     * Отправляемая продукция
     */
    private var productSendDTO = ProductSendDTO()

    /**
     * Продукция, полученная от сервера
     */
    private var productAcceptDTO: ProductAcceptDTO? = null

    /**
     * Список путей до фотографий
     */
    private var images = mutableListOf<Uri>()

    /**
     * Константа для обработки выбора изображений
     */
    private val PICK_IMAGES_REQUEST = 1

    /**
     * Максимальное количество загружаемых фотографий
     */
    private val maxCountPhotos = 5


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveProductBinding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(saveProductBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        saveProductBinding.photosBtn.setOnClickListener {
            openGallery()
        }

        saveProductBinding.saveBtn.setOnClickListener {
            save()
        }
    }

    /**
     * Метод установки стартовых данных
     */
    private fun setStartData() {

        val productId = intent.getLongExtra("productId", 0)
        if(productId != 0L){
            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    productAcceptDTO = productRepository.get(productId)
                    withContext(Dispatchers.Main){
                        if(productAcceptDTO != null){
                            productSendDTO.id = productAcceptDTO!!.id
                            productSendDTO.cost = productAcceptDTO!!.cost

                            saveProductBinding.nameText.setText(productAcceptDTO!!.name)
                            saveProductBinding.nameContainer.helperText = null
                            message.setSuccess(saveProductBinding.photosHelperText, "Текущие изображения сохранены")
                        }

                        delay(100)
                        loadTypeProducts()
                        loadMaterials()
                    }
//                    withContext(Dispatchers.Main){
//                        delay(100)
//                        loadTypeProducts()
//                        loadMaterials()
//                    }
                }catch (e:Exception){
                    message.showError("Ошибка получения информации о продукции", saveProductBinding.root)
                }
            }
        }else{
            loadTypeProducts()
            loadMaterials()
        }
    }

    /**
     * Метод открытия галлереи для выбора фотографий
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        startActivityForResult(intent, PICK_IMAGES_REQUEST)
    }


    /**
     * Метод обработки выбранный фотографий
     * @param[requestCode] код запроса
     * @param[resultCode] код результата
     * @param[data] активность, которая открыла галлерею
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            images = mutableListOf()

            data?.let { intentData ->
                if (intentData.clipData != null) {
                    val count = intentData.clipData!!.itemCount
                    if(count <= maxCountPhotos){
                        for (i in 0 until count) {
                            val imageUri = intentData.clipData!!.getItemAt(i).uri
                            images.add(imageUri)
                            message.setSuccess(saveProductBinding.photosHelperText, "Фотографии выбраны")
                        }
                    }else{
                        message.showError("Разрешается загрузить максимум $maxCountPhotos фотографий", saveProductBinding.root)
                        message.setError(saveProductBinding.photosHelperText, "Необходимо выбрать фотографии")
                    }
                } else if (intentData.data != null) {
                    val imageUri = intentData.data!!
                    images.add(imageUri)
                    message.setSuccess(saveProductBinding.photosHelperText, "Фотография выбрана")
                }else{
                    message.setError(saveProductBinding.photosHelperText, "Необходимо выбрать фотографии")
                }

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

    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners() {
        Listener().nameListener(saveProductBinding.nameText, saveProductBinding.nameContainer)
        listenerOfCountMaterials()
    }

    /**
     * Метод прослушивания изменения данных о выбранных материалах
     */
    private fun listenerOfCountMaterials() {
        for (i in 0 until recyclerViewMaterialsAdapter!!.itemCount) {
            val viewHolder = saveProductBinding.materialsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewMaterialsAddAdapter.ViewHolder?

            viewHolder?.let {
                val editText = it.itemRecyclerViewWithAddBinding.countEditText

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
                                val countMaterials = enteredText.toInt()

                                val minCountMaterials = 1
                                val maxCountMaterials = 1000

                                if (countMaterials < minCountMaterials) {
                                    editText.setText(minCountMaterials.toString())
                                    message.showError("Минимальное количество материала = $minCountMaterials", saveProductBinding.root)
                                } else if (countMaterials > maxCountMaterials) {
                                    editText.setText(maxCountMaterials.toString())
                                    message.showError("Максимальное количество материала = $maxCountMaterials", saveProductBinding.root)
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

    /**
     * Загрузка данных о материалах
     */
    private fun loadMaterials() {
        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val materials = MaterialRepository().get(null, "")
                withContext(Dispatchers.Main){
                    if(materials != null){
                        recyclerViewMaterialsAdapter = RecyclerViewMaterialsAddAdapter(materials)
                        saveProductBinding.materialsRecyclerView.adapter = recyclerViewMaterialsAdapter
                        saveProductBinding.materialsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        saveProductBinding.materialsRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

                        saveProductBinding.materialsRecyclerView.post {
                            if (productAcceptDTO?.productMaterialDTOS != null) {
                                for (i in 0 until recyclerViewMaterialsAdapter!!.itemCount) {
                                    val material = recyclerViewMaterialsAdapter!!.getMaterial(i)

                                    val foundProductMaterialDTO = productAcceptDTO!!.productMaterialDTOS!!.find { it.material.id == material.id }
                                    if (foundProductMaterialDTO != null) {
                                        val viewHolder = saveProductBinding.materialsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewMaterialsAddAdapter.ViewHolder?

                                        viewHolder?.let {
                                            it.itemRecyclerViewWithAddBinding.countEditText.setText(foundProductMaterialDTO.countMaterials.toString())
                                        }
                                    }
                                }
                            }
                        }

                        delay(1000)
                        setListeners()
                        calculateCost()
                        //setCostText(productSendDTO!!.cost)
                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка получения списка материалов", saveProductBinding.root)
            }
        }
    }

    /**
     * Загрузка данных о типах продукции
     */
    private fun loadTypeProducts(){

        lifecycleScope.launch(Dispatchers.IO) {
            try{
                val typeProducts = TypeProductRepository().get(null, "")
                withContext(Dispatchers.Main){
                    if(typeProducts != null) {
                        val spinnerAdapter = TypeProductsSpinnerAdapter(applicationContext, typeProducts)
                        saveProductBinding.typeProductsSpinner.adapter = spinnerAdapter

                        if(productAcceptDTO?.typeProduct != null){
                            val position = spinnerAdapter.getPosition(productAcceptDTO!!.typeProduct)
                            if(position != Spinner.INVALID_POSITION){
                                saveProductBinding.typeProductsSpinner.setSelection(position)
                            }
                        }
                        saveProductBinding.typeProductsSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                productSendDTO!!.typeProduct = spinnerAdapter.getSelectedTypeProduct(position)!!
                                calculateCost()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }else{
                        message.showError("Список типов продукции пуст", saveProductBinding.root)
                    }
                }
            }catch (e:Exception){
                message.showError("Ошибка получения списка типов продукции", saveProductBinding.root)
            }
        }
    }

    /**
     * Метод подсчёта стоимости продукции
     */
    private fun calculateCost() {
        productSendDTO!!.cost = BigDecimal(0)

        for(i in 0 until recyclerViewMaterialsAdapter!!.itemCount){
            val viewHolder = saveProductBinding.materialsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewMaterialsAddAdapter.ViewHolder?

            viewHolder?.let {
                val countEditTextValue = it.itemRecyclerViewWithAddBinding.countEditText.text.toString()

                if(countEditTextValue.isNotEmpty()){
                    val material = recyclerViewMaterialsAdapter!!.getMaterial(i)
                    productSendDTO!!.cost = productSendDTO!!.cost.add(material.cost.multiply(BigDecimal(countEditTextValue.toInt())))
                }
            }
        }

        if(productSendDTO!!.cost != BigDecimal(0)) {
            val coefficientOfIncreaseInCost = 1 + productSendDTO!!.typeProduct!!.margin / 100
            productSendDTO!!.cost = productSendDTO!!.cost.multiply(BigDecimal(coefficientOfIncreaseInCost)).setScale(2, RoundingMode.HALF_UP)

        }

        setCostText(productSendDTO!!.cost)
    }

    /**
     * Метод отображения стоимости в textView
     * @param[cost] стоимость продукции
     */
    private fun setCostText(cost:BigDecimal){
        val costText = SpannableStringBuilder().bold { append("Итоговая стоимость: ") }.append(cost.toString() + "₽")
        saveProductBinding.costView.text = costText
    }

    /**
     * Метод добавления данных о выбранных материалах в список, связывающий продукцию с материалами
     * @return список, связывающий продукцию с материалами
     */
    private fun getProductMaterials(): MutableList<ProductMaterialDTO> {
        var productMaterials: MutableList<ProductMaterialDTO> = mutableListOf()

        for(i in 0 until recyclerViewMaterialsAdapter!!.itemCount){
            val viewHolder = saveProductBinding.materialsRecyclerView.findViewHolderForAdapterPosition(i) as RecyclerViewMaterialsAddAdapter.ViewHolder?

            viewHolder?.let {
                val countEditTextValue = it.itemRecyclerViewWithAddBinding.countEditText.text.toString()

                if(countEditTextValue.isNotEmpty()){
                    val material = recyclerViewMaterialsAdapter!!.getMaterial(i)

                    val productMaterialDTO = ProductMaterialDTO(material, countEditTextValue.toInt())
                    productMaterials.add(productMaterialDTO)

                }
            }
        }

        return productMaterials
    }

    /**
     * Метод сохранения данных о продукции
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun save() {
        if(saveProductBinding.nameContainer.helperText == null && productSendDTO!!.cost != BigDecimal(0) && !saveProductBinding.photosHelperText.text.startsWith("Необходимо")){
            productSendDTO!!.name = saveProductBinding.nameText.text.toString().trim()

            if(productSendDTO!!.cost < BigDecimal.valueOf(1) || productSendDTO!!.cost > BigDecimal.valueOf(100000)){
                message.showError("Стоимость продукции должна входить в диапазон от 1 до 100000 ₽",saveProductBinding.root)
            }else {
                val fileWorker = FileWorker()
                var messageResponse: MessageResponse?
                var files: MutableList<File> = mutableListOf()

                if (productSendDTO!!.id != 0.toLong()) {
                    if(images.size == 0){
                        for(photo in productAcceptDTO!!.photos!!){
                            val file = fileWorker.fromBase64ToFile(photo)
                            if(file == null){
                                message.showError("Ошибка загрузки изображений", saveProductBinding.root)
                                return
                            }else{
                                files.add(file)
                            }
                        }
                    }else{
                        if(!fillingListOfFiles(files)){
                            message.showError("Ошибка загрузки изображений", saveProductBinding.root)
                            return
                        }
                    }

                    productSendDTO!!.userId = JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(R.string.keyForJwtResponse))!!.user.id
                    productSendDTO!!.productMaterialDTOS = getProductMaterials()

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            withContext(Dispatchers.Main) {
                                saveProductBinding.progressBar.visibility = View.VISIBLE
                            }

                            messageResponse = productRepository.update(productSendDTO!!, files)
                            if (messageResponse != null) {
                                if (messageResponse!!.code != 200) {
                                    message.showError(messageResponse!!.message, saveProductBinding.root)
                                } else {
                                    message.showSuccess(messageResponse!!.message, saveProductBinding.root)
                                    delay(1000)
                                    goToListProducts()
                                }
                            }
                        }catch (e:Exception){
                            message.showError("Ошибка изменения данных о продукции", saveProductBinding.root)
                        }

                        runOnUiThread {
                            saveProductBinding.progressBar.visibility = View.INVISIBLE
                        }
                    }

                } else {
                    if (fillingListOfFiles(files)) {
                        productSendDTO!!.userId = JwtResponse.getFromMemory(applicationContext, applicationContext.resources.getString(R.string.keyForJwtResponse))!!.user.id
                        productSendDTO!!.productMaterialDTOS = getProductMaterials()

                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                withContext(Dispatchers.Main) {
                                    saveProductBinding.progressBar.visibility = View.VISIBLE
                                }

                                messageResponse = productRepository.add(productSendDTO!!, files)
                                if (messageResponse != null) {
                                    if (messageResponse!!.code == 400) {
                                        message.showError(messageResponse!!.message, saveProductBinding.root)
                                    } else if (messageResponse!!.code == 200) {
                                        message.showSuccess(messageResponse!!.message, saveProductBinding.root)
                                        delay(1000)
                                        goToListProducts()
                                    }
                                }
                            }catch (e:Exception){
                                message.showError("Ошибка добавления продукции", saveProductBinding.root)
                            }

                            runOnUiThread {
                                saveProductBinding.progressBar.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        message.showError("Ошибка получения изображений. Повторите попытку", saveProductBinding.root)
                    }

                }
            }
        }else{
            message.showError("Некорректный ввод. Повторите попытку.", saveProductBinding.root)
        }
    }

    /**
     * Метод заполнения списка файлов
     * @param[files] пустой список, в который будут заноситься фотографии
     * @return Все ли фотографии были успешно заносены в список
     */
    private fun fillingListOfFiles(files: MutableList<File>):Boolean{
        val fileWorker = FileWorker()

        for (imageUri in images) {
            val file = fileWorker.uriToFile(imageUri, applicationContext)
            if (file == null) {
                return false
            } else {
                files.add(file)
            }
        }

        return true
    }

    /**
     * Метод перехода к списку продукций
     */
    private fun goToListProducts(){
        val intent = Intent(this@SaveProductActivity, MainActivity::class.java)
        intent.putExtra("fragment", "ProductFragment")
        startActivity(intent)
    }
}