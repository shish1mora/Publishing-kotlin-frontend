package com.example.publishinghousekotlin.basics

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

/**
 * Класс для отображения сообщений пользователю
 * @author Денис
 * @since 1.0.0
 */
class Messages {

    /**
     * Отображает Snackbar с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке, которое будет отображено в Snackbar.
     * @param root Корневой контейнер, к которому привязан Snackbar.
     */
    fun showError(message:String, root: ViewGroup){
        val snackbar = Snackbar.make(root,message, Snackbar.LENGTH_LONG)

        val textViewOfSnackBar: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
        textViewOfSnackBar.setTextColor(Color.parseColor("#BB4444"))

        snackbar.show()
    }

    /**
     * Отображает Snackbar с сообщением об успешном действии.
     *
     * @param message Сообщение об успешном действии, которое будет отображено в Snackbar.
     * @param root Корневой контейнер, к которому привязан Snackbar.
     */
    fun showSuccess(message: String, root: ViewGroup){
        val snackbar = Snackbar.make(root,message, Snackbar.LENGTH_LONG)

        val textViewOfSnackBar: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
        textViewOfSnackBar.setTextColor(Color.parseColor("#00FF00"))

        snackbar.show()
    }

    /**
     * Устанавливает текст в TextView с цветом успешного действия.
     *
     * @param textView TextView, в котором нужно установить текст.
     * @param message Текст, который будет установлен в TextView.
     */
    fun setSuccess(textView: TextView, message:String){
        textView.text = message
        textView.setTextColor(Color.parseColor("#00FF00"))
    }

    /**
     * Устанавливает текст в TextView с цветом сообщения об ошибке.
     *
     * @param textView TextView, в котором нужно установить текст.
     * @param message Текст, который будет установлен в TextView.
     */
    fun setError(textView: TextView, message: String){
        textView.text = message
        textView.setTextColor(Color.parseColor("#BB4444"))
    }
}