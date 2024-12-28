package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.example.publishinghousekotlin.basics.FileWorker
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.databinding.DetailsEmployeeBinding
import com.example.publishinghousekotlin.dialogs.DeleteEmployeeDialog
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import java.time.format.DateTimeFormatter

/**
 * Activity для вывода подробной информации о сотруднике
 *
 * @author Климачков Даниил
 * @since 1.0.0
 */
class DetailsEmployeeActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var detailsEmployeeBinding: DetailsEmployeeBinding

    /**
     * Сотрудник, данные о котором будут выводиться
     */
    private var employeeDTO: EmployeeDTO? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailsEmployeeBinding = DetailsEmployeeBinding.inflate(layoutInflater)
        setContentView(detailsEmployeeBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        detailsEmployeeBinding.deleteBtn.setOnClickListener {
            DeleteEmployeeDialog(employeeDTO!!.id, detailsEmployeeBinding.root).show(supportFragmentManager, "DELETEEMPLOYEEDIALOG")
        }

        detailsEmployeeBinding.editBtn.setOnClickListener {
            val intent = Intent(this@DetailsEmployeeActivity, SaveEmployeeActivity::class.java)
            intent.putExtra("employee", employeeDTO)
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
    private fun setStartData() {
        try {
            employeeDTO = intent.getSerializableExtra("employee") as? EmployeeDTO

            val surname = SpannableStringBuilder().bold { append("Фамилия: ") }.append(employeeDTO?.surname)
            val name = SpannableStringBuilder().bold { append("Имя: ") }.append(employeeDTO?.name)

            val patronymicAppend = if (employeeDTO?.patronymic!!.isEmpty()) "отсутствует" else employeeDTO?.patronymic
            val patronymic = SpannableStringBuilder().bold { append("Отчество: ") }.append(patronymicAppend)
            val birthday = SpannableStringBuilder().bold { append("Дата рождения: ") }.append(employeeDTO?.birthday!!.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            val post = SpannableStringBuilder().bold { append("Должность: ") }.append(employeeDTO?.post)
            val email = SpannableStringBuilder().bold { append("Электронная почта: ") }.append(employeeDTO?.email)
            val phone = SpannableStringBuilder().bold { append("Номер телефона: ") }.append(employeeDTO?.phone)

            detailsEmployeeBinding.surnameTextView.text = surname
            detailsEmployeeBinding.nameTextView.text = name
            detailsEmployeeBinding.patronymicTextView.text = patronymic
            detailsEmployeeBinding.birthdayTextView.text = birthday
            detailsEmployeeBinding.postTextView.text = post
            detailsEmployeeBinding.emailTextView.text = email
            detailsEmployeeBinding.phoneTextView.text = phone
            detailsEmployeeBinding.photoView.setImageBitmap(FileWorker().getBitmap(employeeDTO!!.photo))
        } catch (e: Exception){
            Messages().showError("Ошибка получения данных о сотруднике", detailsEmployeeBinding.root)
        }


    }


}