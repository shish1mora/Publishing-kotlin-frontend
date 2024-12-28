package com.example.publishinghousekotlin.controllers


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.basics.Listener
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.ActivitySaveEmployeeBinding
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.Employee
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.repositories.EmployeeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.time.Period

/**
 * Activity для добавления/изменения сотрудника
 *
 * @author Денис
 * @since 1.0.0
 */
class SaveEmployeeActivity:AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var saveEmployeeBinding: ActivitySaveEmployeeBinding

    /**
     * Экземпляр класса для отправки сообщений пользователю
     */
    private var message = Messages()

    /**
     * Поле для выбора изображения
     */
    private var imagePickerLauncher: ActivityResultLauncher<Intent>? = null

    /**
     * Путь до выбранной фотографии
     */
    private var selectedImageUri: Uri? = null

    /**
     * Сотрудник, который будет добавляться/изменяться
     */
    private var employee: Employee? = null

    /**
     * Фотография сотрудника в виде Base64String
     */
    private var base64String: String = ""


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveEmployeeBinding = ActivitySaveEmployeeBinding.inflate(layoutInflater)
        setContentView(saveEmployeeBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()
        waitingPhoto()
        setListeners()

        saveEmployeeBinding.saveBtn.setOnClickListener {
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
        val employeeDTO = intent.getSerializableExtra("employee") as? EmployeeDTO

        if(employeeDTO!!.surname != ""){
            saveEmployeeBinding.surnameText.setText(employeeDTO!!.surname)
            saveEmployeeBinding.surnameContainer.helperText = null
        }
        if(employeeDTO!!.name != ""){
            saveEmployeeBinding.nameText.setText(employeeDTO!!.name)
            saveEmployeeBinding.nameContainer.helperText = null
        }
        if(employeeDTO!!.phone != ""){
            saveEmployeeBinding.phoneText.setText(employeeDTO!!.phone)
            saveEmployeeBinding.phoneContainer.helperText = null
        }
        if(employeeDTO!!.email != ""){
            saveEmployeeBinding.emailText.setText(employeeDTO!!.email)
            saveEmployeeBinding.emailContainer.helperText = null
        }
        if(employeeDTO!!.post != ""){
            saveEmployeeBinding.postText.setText(employeeDTO!!.post)
            saveEmployeeBinding.postContainer.helperText = null
        }
        if(employeeDTO!!.photo.isNotEmpty()){
            message.setSuccess(saveEmployeeBinding.photoHelperText, "Текущее изображение сохранено")
        }

        saveEmployeeBinding.patronymicText.setText(employeeDTO!!.patronymic)

        employee = Employee(employeeDTO.id, employeeDTO.surname, employeeDTO.name, employeeDTO.patronymic, employeeDTO.phone, employeeDTO.email, employeeDTO.post, employeeDTO.pathToImage, employeeDTO.birthday)
        base64String = employeeDTO.photo

        setBirthday()
        openGallery()
    }

    /**
     * Метод выбора и отображения даты рождения
     */
    private fun setBirthday() {

        if(employee!!.birthday != LocalDate.now()){
            message.setSuccess(saveEmployeeBinding.birthdayHelperText, "Текущая дата рождения сохранена")
        }

        saveEmployeeBinding.birthdayButton.setOnClickListener {

            val year = employee!!.birthday!!.year
            val month = employee!!.birthday!!.month.value - 1
            val day = employee!!.birthday!!.dayOfMonth

            val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->

                employee!!.birthday = LocalDate.of(selectedYear,selectedMonth + 1,selectedDay)
                val period = Period.between(employee!!.birthday, LocalDate.now())
                if(period.years < 16){
                    message.setError(saveEmployeeBinding.birthdayHelperText, "Минимальный возраст сотрудника: 16 лет")
                }else {
                    message.setSuccess(saveEmployeeBinding.birthdayHelperText, "Дата рождения выбрана")
                }
            },year,month,day)

            datePickerDialog.show()

        }
    }

    /**
     * Метод перехода к выбору фотографий
     */
    private fun openGallery() {
        saveEmployeeBinding.photoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher?.launch(intent)
        }
    }

    /**
     * Метод получения пути до фотографии
     */
    private fun waitingPhoto() {
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data
                if(selectedImageUri != null){
                    message.setSuccess(saveEmployeeBinding.photoHelperText, "Изображение выбрано")
                }else{
                    message.setError(saveEmployeeBinding.photoHelperText, "Необходимо выбрать фото")
                }
            }
        }
    }

    /**
     * Метод установки прослушивания изменения текста в EditTexts
     */
    private fun setListeners() {
        val listener = Listener()

        listener.russianWordsListener(1, 20, saveEmployeeBinding.nameText, saveEmployeeBinding.nameContainer)
        listener.russianWordsListener(1,20,saveEmployeeBinding.surnameText, saveEmployeeBinding.surnameContainer)
        listener.russianWordsListener(20, saveEmployeeBinding.patronymicText, saveEmployeeBinding.patronymicContainer)
        listener.emailListener(saveEmployeeBinding.emailText, saveEmployeeBinding.emailContainer)
        listener.phoneListener(saveEmployeeBinding.phoneText, saveEmployeeBinding.phoneContainer)
        listener.russianWordsListener(3,25, saveEmployeeBinding.postText, saveEmployeeBinding.postContainer)

    }

    /**
     * Метод сохранения данных о сотруднике
     * @exception[Exception] Ошибка при отправки данных на сервер
     */
    private fun save() {
       if(saveEmployeeBinding.surnameContainer.helperText == null && saveEmployeeBinding.nameContainer.helperText == null && saveEmployeeBinding.patronymicContainer.helperText == null && saveEmployeeBinding.phoneContainer.helperText == null &&
           saveEmployeeBinding.emailContainer.helperText == null && saveEmployeeBinding.postContainer.helperText == null && !saveEmployeeBinding.birthdayHelperText.text.startsWith("Необходимо")
           && !saveEmployeeBinding.birthdayHelperText.text.startsWith("Минимальный возраст") && !saveEmployeeBinding.photoHelperText.text.startsWith("Необходимо")) {

           employee!!.surname = saveEmployeeBinding.surnameText.text.toString().trim()
           employee!!.name = saveEmployeeBinding.nameText.text.toString().trim()
           employee!!.patronymic = saveEmployeeBinding.patronymicText.text.toString().trim()
           employee!!.phone = saveEmployeeBinding.phoneText.text.toString().trim()
           employee!!.email = saveEmployeeBinding.emailText.text.toString().trim()
           employee!!.post = saveEmployeeBinding.postText.text.toString().trim()

           val employeeRepository = EmployeeRepository()
           var messageResponse: MessageResponse?
           var fileWorker = FileWorker()
           var photo:File?

           if (employee!!.id != 0.toLong()) {
               lifecycleScope.launch(Dispatchers.IO) {
                   try{
                       withContext(Dispatchers.Main){
                           saveEmployeeBinding.progressBar.visibility = View.VISIBLE
                       }

                       photo = if(selectedImageUri == null){
                           fileWorker.fromBase64ToFile(base64String)
                       }else{
                           FileWorker().uriToFile(selectedImageUri!!, applicationContext)
                       }

                       if(photo != null){
                            messageResponse = employeeRepository.update(employee!!, photo!!)
                            if(messageResponse != null){
                                if (messageResponse?.code != 200) {
                                    message.showError(messageResponse!!.message, saveEmployeeBinding.root)
                                }else{
                                    message.showSuccess(messageResponse!!.message, saveEmployeeBinding.root)

                                    delay(1000)
                                    goToListEmployees()
                                }
                            }
                       }else{
                           message.showError("Выбранного изображения не существует", saveEmployeeBinding.root)
                       }
                   }catch (e:Exception){
                       message.showError("Ошибка изменения данных о сотруднике. Повторите попытку",saveEmployeeBinding.root)
                   }
                   runOnUiThread {
                       saveEmployeeBinding.progressBar.visibility = View.INVISIBLE
                   }
               }
           } else {
               lifecycleScope.launch(Dispatchers.IO) {
                   try {
                       delay(1000)
                       withContext(Dispatchers.Main) {
                           saveEmployeeBinding.progressBar.visibility = View.VISIBLE
                       }
                        photo = fileWorker.uriToFile(selectedImageUri!!, applicationContext)
                        if(photo != null) {
                            messageResponse = employeeRepository.add(employee!!, photo!!)
                            if (messageResponse != null) {
                                if (messageResponse?.code == 400 || messageResponse?.code == 409) {
                                    message.showError(messageResponse!!.message, saveEmployeeBinding.root)
                                } else if (messageResponse?.code == 200) {
                                    message.showSuccess(messageResponse!!.message, saveEmployeeBinding.root)

                                    delay(1000)
                                    goToListEmployees()
                                }
                            }
                        }else{
                            message.showError("Выбранного изображения не существует", saveEmployeeBinding.root)
                        }
                   } catch (e: Exception) {
                       message.showError("Ошибка добавления сотрудника. Повторите попытку", saveEmployeeBinding.root)
                   }
                   runOnUiThread {
                       saveEmployeeBinding.progressBar.visibility = View.INVISIBLE
                   }
               }
           }
       } else {
           message.showError("Некорректный ввод. Повторите попытку.", saveEmployeeBinding.root)
       }
    }

    /**
     * Метод перехода к списку сотрудников
     */
    private fun goToListEmployees() {
        val intent = Intent(this@SaveEmployeeActivity, MainActivity::class.java)
        intent.putExtra("fragment", "EmployeeFragment")
        startActivity(intent)
    }

}