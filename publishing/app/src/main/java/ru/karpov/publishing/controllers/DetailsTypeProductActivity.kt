package com.example.publishinghousekotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.example.publishinghousekotlin.databinding.DetailsTypeProductBinding
import com.example.publishinghousekotlin.dialogs.DeleteTypeProductDialog
import com.example.publishinghousekotlin.models.TypeProduct


/**
 * Activity для вывода подробной информации о типе продукции
 *
 * @author Денис
 * @since 1.0.0
 */
class DetailsTypeProductActivity: AppCompatActivity() {


    /**
     * Биндинг для доступа к компонентам
     */
    private lateinit var detailsTypeProductBinding: DetailsTypeProductBinding


    /**
     * Тип продукции, данные о котором будут выводиться
     */
    private var typeProduct:TypeProduct? = null


    /**
     * Переопределение метода onCreate()
     * @param[savedInstanceState] ссылка на объект Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsTypeProductBinding = DetailsTypeProductBinding.inflate(layoutInflater)
        setContentView(detailsTypeProductBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setStartData()

        detailsTypeProductBinding.editBtn.setOnClickListener {
            val intent = Intent(this@DetailsTypeProductActivity, SaveTypeProductActivity::class.java)
            intent.putExtra("typeProduct", typeProduct)
            startActivity(intent)
        }

        detailsTypeProductBinding.deleteBtn.setOnClickListener {
            DeleteTypeProductDialog(typeProduct!!.id,detailsTypeProductBinding.root).show(supportFragmentManager, "DELETETYPEPRODUCTDIALOG")
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

        val type = SpannableStringBuilder().bold { append("Тип: ") }.append(typeProduct?.type)
        val margin = SpannableStringBuilder().bold { append("Наценка в %: ") }.append(typeProduct?.margin.toString())

        detailsTypeProductBinding.typeTextView.text = type
        detailsTypeProductBinding.marginTextView.text = margin
    }

}