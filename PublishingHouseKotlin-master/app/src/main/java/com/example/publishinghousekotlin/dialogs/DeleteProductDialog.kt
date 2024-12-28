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
import com.example.publishinghousekotlin.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Класс представляет собой диалоговое окно для подтверждения удаления продукции.
 *
 * @property productId Идентификатор продукции, которая подлежит удалению.
 * @property root Корневой контейнер, используемый для отображения сообщений об успешном или неудачном удалении продукции.
 */
class DeleteProductDialog(private val productId: Long, private val root: ViewGroup):DialogFragment() {

    /**
     * Метод, создающий диалоговое окно для подтверждения удаления продукции.
     *
     * При подтверждении удаляет продукцию и возвращает к списку продукций.
     * @param savedInstanceState Сохраненное состояние фрагмента.
     * @throws Exception Если произошла ошибка работы с сервером
     * @return Возвращает созданное диалоговое окно.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("Удаление продукции")
            .setMessage("Вы подтверждаете удаление этой продукции?")
            .setPositiveButton("Подтвердить", null)
            .setNegativeButton("Отмена", {_,_->})
            .create().apply {
                setOnShowListener {
                    val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error))

                    positiveButton.setOnClickListener {
                        positiveButton.isEnabled = false
                        val message = Messages()

                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                val messageResponse = ProductRepository().delete(productId)
                                if (messageResponse != null) {
                                    if (messageResponse.code == 409) {
                                        message.showError(messageResponse.message, root)

                                        delay(500)
                                        dismiss()
                                    } else if (messageResponse.code == 204) {
                                        message.showSuccess("Продукция успешно удалена!", root)

                                        delay(500)
                                        val intent = Intent(activity, MainActivity::class.java)
                                        intent.putExtra("fragment", "ProductFragment")
                                        startActivity(intent)
                                    }
                                }
                            } catch (e: Exception) {
                                message.showError("Ошибка удаления продукции. Повторите попытку", root)

                                delay(500)
                                dismiss()
                            }
                        }
                    }
                }
            }
    }
}