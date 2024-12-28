package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.example.publishinghousekotlin.databinding.DetailsMaterialBinding
import com.example.publishinghousekotlin.dialogs.DeleteMaterialDialog
import com.example.publishinghousekotlin.models.Material


/**
 * Activity для вывода подробной информации о материале
 *
 * @author Денис
 * @since 1.0.0
 */
class DetailsMaterialActivity: AppCompatActivity() {

    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var detailsMaterialBinding: DetailsMaterialBinding

    /**
     * Материал, данные о котором будут выводиться
     */
    private var material: Material? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsMaterialBinding = DetailsMaterialBinding.inflate(layoutInflater)
        setContentView(detailsMaterialBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        detailsMaterialBinding.editBtn.setOnClickListener {
            val intent = Intent(this@DetailsMaterialActivity, SaveMaterialActivity::class.java)
            intent.putExtra("material",material)
            startActivity(intent)
        }

        detailsMaterialBinding.deleteBtn.setOnClickListener {
            DeleteMaterialDialog(material!!.id, detailsMaterialBinding.root).show(supportFragmentManager,"DELETEMATERIALDIALOG")
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
        material = intent.getSerializableExtra("material") as? Material

        val type = SpannableStringBuilder().bold { append("Тип: ") }.append(material?.type)
        val size = SpannableStringBuilder().bold { append("Размер: ") }.append(material?.size)
        val color = SpannableStringBuilder().bold { append("Цвет в RGB: ") }.append(material?.color)
        val cost = SpannableStringBuilder().bold { append("Стоимость в ₽: ") }.append(material?.cost.toString())

        detailsMaterialBinding.typeTextView.text = type
        detailsMaterialBinding.sizeTextView.text = size
        detailsMaterialBinding.colorTextView.text = color
        detailsMaterialBinding.costTextView.text = cost
    }


}