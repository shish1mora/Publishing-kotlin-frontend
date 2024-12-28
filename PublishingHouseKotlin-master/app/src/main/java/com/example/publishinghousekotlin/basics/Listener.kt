package com.example.publishinghousekotlin.basics

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Класс, предоставляющий методы для прослушивания ввода в различных полях ввода с целью валидации данных.
 * @author Денис
 * @since 1.0.0
 */
class Listener {

    /**
     * Экземпляр класса, предназначенный для валидации данных
     */
    private var validator = Validator()


    /**
     * Прослушиватель для поля ввода электронной почты.
     *
     * @param emailText Поле ввода электронной почты.
     * @param emailContainer Контейнер для отображения подсказок или ошибок по введенной электронной почте.
     */
    fun emailListener(emailText: TextInputEditText, emailContainer: TextInputLayout){
        emailText.addTextChangedListener {
            emailContainer.helperText = validator.isValidEmail(emailText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода пароля.
     *
     * @param passwordText Поле ввода пароля.
     * @param passwordContainer Контейнер для отображения подсказок или ошибок по введенному паролю.
     */
    fun passwordListener(passwordText: TextInputEditText,passwordContainer: TextInputLayout){
        passwordText.addTextChangedListener {
            passwordContainer.helperText = validator.isValidPassword(passwordText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода подтверждаемого пароля.
     *
     * @param passwordText Поле ввода пароля.
     * @param confirmPasswordText Поле ввода подтверждаемого пароля.
     * @param confirmPasswordContainer Контейнер для отображения подсказок или ошибок по введенному паролю.
     */
    fun confirmPasswordListener(passwordText:TextInputEditText, confirmPasswordText: TextInputEditText, confirmPasswordContainer: TextInputLayout){
        confirmPasswordText.addTextChangedListener {
            confirmPasswordContainer.helperText = validator.isValidConfirmPassword(passwordText.text.toString(), confirmPasswordText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода пароля с валидацией на равенство с подтверждаемым паролем.
     *
     * @param passwordText Поле ввода пароля.
     * @param confirmPasswordText Поле ввода подтверждаемого пароля.
     * @param confirmPasswordContainer Контейнер для отображения подсказок или ошибок по введенному паролю.
     */
    fun passwordAndConfirmPasswordListener(passwordText: TextInputEditText, confirmPasswordText: TextInputEditText, passwordContainer: TextInputLayout, confirmPasswordContainer: TextInputLayout){
        passwordText.addTextChangedListener{
            passwordContainer.helperText = validator.isValidPassword(passwordText.text.toString())
            confirmPasswordContainer.helperText = validator.isValidConfirmPassword(passwordText.text.toString(), confirmPasswordText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода наименования.
     *
     * @param nameText Поле ввода наименования.
     * @param nameContainer Контейнер для отображения подсказок или ошибок по введенному наименованию.
     */
    fun nameListener(nameText: TextInputEditText, nameContainer: TextInputLayout){
        nameText.addTextChangedListener {
            nameContainer.helperText = validator.isValidName(nameText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода номера телефона.
     *
     * @param phoneText Поле ввода номера телефона.
     * @param phoneContainer Контейнер для отображения подсказок или ошибок по введенному номеру телефона.
     */
    fun phoneListener(phoneText: TextInputEditText, phoneContainer: TextInputLayout){
        phoneText.addTextChangedListener(object : TextWatcher{
            var beforeLength: Int = 0

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                beforeLength = phoneText.text.toString().length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val digits = phoneText.text.toString().length

                if(beforeLength < digits) {
                    if (digits == 6 || digits == 10 || digits == 13) {
                        phoneText.setText(phoneText.text.toString() + "-")
                    }
                }
                else {
                    if (digits < 3) {
                        phoneText.setText("+7-")
                    }
                }

                phoneText.setSelection(phoneText.length())
                phoneContainer.helperText = validator.isValidPhone(phoneText.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    /**
     * Прослушиватель для поля ввода русских слов.
     *
     * @param minLengthOfWord Минимальная длина слова.
     * @param maxLengthOfWord Максимальная длина слова.
     * @param russianWordsText Поле ввода русского слова.
     * @param russianWordsContainer Контейнер для отображения подсказок или ошибок по введенному русскому слову.
     */
    fun russianWordsListener(minLengthOfWord: Int, maxLengthOfWord: Int, russianWordsText: TextInputEditText, russianWordsContainer: TextInputLayout){
        russianWordsText.addTextChangedListener {
            russianWordsContainer.helperText = validator.isValidRussianWord(russianWordsText.text.toString(), minLengthOfWord, maxLengthOfWord)
        }
    }

    /**
     * Прослушиватель для поля ввода русских слов.
     * @param maxLengthOfWord Максимальная длина слова.
     * @param russianWordsText Поле ввода русского слова.
     * @param russianWordsContainer Контейнер для отображения подсказок или ошибок по введенному русскому слову.
     */
    fun russianWordsListener(maxLengthOfWord: Int, russianWordsText: TextInputEditText, russianWordsContainer: TextInputLayout){
        russianWordsText.addTextChangedListener {
            if(russianWordsText.text!!.isNotEmpty()) {
                russianWordsContainer.helperText = validator.isValidRussianWord(russianWordsText.text.toString(),0, maxLengthOfWord)
            }else{
                russianWordsContainer.helperText = null
            }
        }
    }

    /**
     * Прослушиватель для поля ввода double значения.
     * @param minValue Минимальное значение double.
     * @param maxValue Максимальнео значение double.
     * @param doubleAsText Поле ввода русского слова.
     * @param doubleContainer Контейнер для отображения подсказок или ошибок по введенному русскому слову.
     */
    fun doubleNumberListener(minValue: Double, maxValue: Double, doubleAsText: TextInputEditText, doubleContainer: TextInputLayout){
        doubleAsText.addTextChangedListener {
            val doubleValue = doubleAsText.text.toString()

            if(doubleValue != "") {
                if(doubleValue.toCharArray()[0] == '.'){
                    doubleAsText.setText("")
                }else {
                    doubleContainer.helperText = validator.isValidDoubleValue(
                        doubleAsText.text.toString().toDouble(),
                        minValue,
                        maxValue
                    )
                }
            }else{
                doubleContainer.helperText = "Необходимо ввести"
            }
        }
    }


    /**
     * Прослушиватель для поля ввода цвета материала.
     *
     * @param colorText Поле ввода цвета материала.
     * @param colorContainer Контейнер для отображения подсказок или ошибок по введенному цвету материала.
     */
    fun colorListener(colorText:TextInputEditText, colorContainer:TextInputLayout){
        colorText.addTextChangedListener {
            colorContainer.helperText = validator.isValidColor(colorText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода размера материала.
     *
     * @param sizeText Поле ввода размера материала.
     * @param sizeContainer Контейнер для отображения подсказок или ошибок по введенному размеру материала.
     */
    fun sizeListener(sizeText: TextInputEditText, sizeContainer: TextInputLayout){
        sizeText.addTextChangedListener {
            sizeContainer.helperText = validator.isValidSizeOfMaterial(sizeText.text.toString())
        }
    }


    /**
     * Прослушиватель для поля ввода города.
     *
     * @param cityText Поле ввода города.
     * @param cityContainer Контейнер для отображения подсказок или ошибок по введенному городу.
     */
    fun cityListener(cityText:TextInputEditText, cityContainer: TextInputLayout){
        cityText.addTextChangedListener{
            cityContainer.helperText = validator.isValidCity(cityText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода улицы.
     *
     * @param streetText Поле ввода улицы.
     * @param streetContainer Контейнер для отображения подсказок или ошибок по введенной улицы.
     */
    fun streetListener(streetText: TextInputEditText, streetContainer: TextInputLayout){
        streetText.addTextChangedListener{
            streetContainer.helperText = validator.isValidStreet(streetText.text.toString())
        }
    }

    /**
     * Прослушиватель для поля ввода номера дома.
     *
     * @param houseNumberText Поле ввода номера дома.
     * @param houseNumberContainer Контейнер для отображения подсказок или ошибок по введенному номеру дома.
     */
    fun houseNumberListener(houseNumberText: TextInputEditText, houseNumberContainer: TextInputLayout){
        houseNumberText.addTextChangedListener {
            houseNumberContainer.helperText = validator.isValidHouseNumber(houseNumberText.text.toString())
        }
    }
}