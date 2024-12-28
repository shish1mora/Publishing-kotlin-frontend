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
import com.example.publishinghousekotlin.dtos.BookingSendDTO
import com.example.publishinghousekotlin.repositories.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class CompleteBookingDialog(private var bookingId: Long, private var root: ViewGroup): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("Выполнение заказа")
            .setMessage("Вы подтверждаете выполнение заказа №$bookingId?")
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
                                val bookingSendDTO = BookingSendDTO(bookingId, "выполняется", LocalDate.now(), null, BigDecimal(1), null, null, null)
                                val messageResponse = BookingRepository().update(bookingSendDTO, "end")
                                if(messageResponse != null){

                                    if(messageResponse.code != 200){
                                        messages.showError(messageResponse.message, root)

                                        delay(500)
                                        dismiss()
                                    }else{
                                        messages.showSuccess("Подтверждение прошло успешно!",root)

                                        delay(500)
                                        val intent = Intent(activity, MainActivity::class.java)
                                        intent.putExtra("fragment", "BookingFragment")
                                        startActivity(intent)
                                    }
                                }
                            }catch (e:Exception){
                                messages.showError("Ошибка подтверждения. Повторите попытку", root)

                                delay(500)
                                dismiss()
                            }
                        }
                    }
                }
            }
    }
}