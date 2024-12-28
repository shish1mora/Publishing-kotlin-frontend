package com.example.publishinghousekotlin.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.publishinghousekotlin.basics.Messages
import com.example.publishinghousekotlin.controllers.MainActivity
import com.example.publishinghousekotlin.repositories.PrintingHouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Класс представляет собой диалоговое окно для подтверждения удаления типографии.
 *
 * @property employeeId Идентификатор типографии, которая подлежит удалению.
 * @property root Корневой контейнер, используемый для отображения сообщений об успешном или неудачном удалении типографии.
 */
class DeletePrintingHouseDialog(private var printingHouseId: Long, private var root:ViewGroup):DialogFragment() {

    /**
     * Метод, создающий диалоговое окно для подтверждения удаления типографии.
     *
     * При подтверждении удаляет типографию и возвращает к списку типографий.
     * @param savedInstanceState Сохраненное состояние фрагмента.
     * @throws Exception Если произошла ошибка работы с сервером
     * @return Возвращает созданное диалоговое окно.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("Удаление типографии")
            .setMessage("Вы подтверждаете удаление этой типографии?")
            .setPositiveButton("Подтвердить", null)
            .setNegativeButton("Отмена", {_,_->})
            .create().apply {
                setOnShowListener {
                    val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error))

                    positiveButton.setOnClickListener {
                        positiveButton.isEnabled = false
                        val messages = Messages()

                        lifecycleScope.launch(Dispatchers.IO) {
                            try{
                                val messageResponse = PrintingHouseRepository().delete(printingHouseId)
                                if(messageResponse != null){

                                    if(messageResponse.code == 409){
                                        messages.showError(messageResponse.message, root)

                                        delay(500)
                                        dismiss()
                                    }else if(messageResponse.code == 204){
                                        messages.showSuccess("Типография успешно удалена!",root)

                                        delay(500)
                                        val intent = Intent(activity, MainActivity::class.java)
                                        intent.putExtra("fragment", "PrintingHouseFragment")
                                        startActivity(intent)
                                    }
                                }
                            }catch (e:Exception){
                                messages.showError("Ошибка удаления типографии. Повторите попытку",root)

                                delay(500)
                                dismiss()
                            }
                        }

                    }
                }
            }
    }
}