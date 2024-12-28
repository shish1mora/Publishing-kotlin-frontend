package com.example.publishinghousekotlin.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.controllers.MainActivity
import com.example.publishinghousekotlin.repositories.TypeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Класс представляет собой диалоговое окно для подтверждения удаления типа продукции.
 *
 * @property employeeId Идентификатор сотрудника, который подлежит удалению.
 * @property root Корневой контейнер, используемый для отображения сообщений об успешном или неудачном удалении сотрудника.
 */
class DeleteTypeProductDialog(private var typeProductId: Long, private var root: ViewGroup) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("Удаление типа продукции")
            .setMessage("Вы подтверждаете удаление этого типа продукции?")
            .setPositiveButton("Подтвердить", null)
            .setNegativeButton("Отмена", {_, _ ->})
            .create().apply {
                setOnShowListener {
                    val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error))

                    positiveButton.setOnClickListener {
                        positiveButton.isEnabled = false
                        val messages = Messages()

                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                val messageResponse = TypeProductRepository().delete(typeProductId)
                                if(messageResponse != null){

                                    if(messageResponse.code == 409) {
                                        messages.showError(messageResponse.message, root)

                                        delay(500)
                                        dismiss()
                                    } else if(messageResponse.code == 204){
                                        messages.showSuccess("Тип продукции успешно удалён!", root)

                                        delay(500)
                                        val intent = Intent(activity, MainActivity::class.java)
                                        intent.putExtra("fragment", "TypeProductFragment")
                                        startActivity(intent)
                                    }
                                }
                            } catch (e: Exception){
                                messages.showError("Ошибка удаления типа продукции. Повторите попытку",root)

                                delay(500)
                                dismiss()
                            }

                        }
                    }
                }
            }
    }
}