package com.example.publishinghousekotlin.basics

/**
 * Класс, предоставляющий методы для валидации различных типов данных.
 * @author Денис
 * @since 1.0.0
 */
class Validator {

    /**
     * Метод, проверяющий является ли заданная строка корректным адресом электронной почты.
     *
     * @param email Проверяемая строка.
     * @return Сообщение об ошибке или null, если адрес электронной почты корректен.
     */
    fun isValidEmail(email:String): String?{

        if(email.length < 5){
            return "Минимальная длина: 5 символов"
        }

        if(email.length > 60) {
            return "Максимальная длина: 60 символов"
        }

        val emailRegex = Regex("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}")
        if(!email.matches(emailRegex)){
            return "Некорректный ввод электронной почты"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным паролем.
     *
     * @param password Проверяемая строка.
     * @return Сообщение об ошибке или null, если пароль корректен.
     */
    fun isValidPassword(password:String): String?{
        if(password.length < 8){
            return "Минимальная длина: 8 символов"
        }

        if(password.length > 30){
            return "Максимальная длина: 30 символов"
        }

        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$")
        if(!password.matches(passwordRegex)){
            return "Пароль должен иметь строчную, пропискную букву и цифру"
        }

        return null
    }

    /**
     * Метод, проверяющий совпадают ли две строки паролей.
     *
     * @param password Первый пароль.
     * @param confirmPassword Подтверждение пароля.
     * @return Сообщение об ошибке или null, если пароли совпадают.
     */
    fun isValidConfirmPassword(password:String?, confirmPassword:String?): String?{
        if(!confirmPassword.equals(password)){
            return "Пароли не совпадают"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным наименованием.
     *
     * @param name Проверяемая строка.
     * @return Сообщение об ошибке или null, если наименование корректно.
     */
    fun isValidName(name:String): String?{
        if(name.length == 0){
            return "Необходимо ввести наименование"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным номером телефона.
     *
     * @param phone Проверяемая строка.
     * @return Сообщение об ошибке или null, если номер телефона корректен.
     */
    fun isValidPhone(phone:String): String?{
        val phoneRegex = Regex("\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}")

        if(!phone.matches(phoneRegex)){
            return "Формат номера телефона: +7-###-###-##-##"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным русским словом.
     *
     * @param word Проверяемая строка.
     * @param minLength минимальная длина строки
     * @param maxLength максимальная длина строки
     * @return Сообщение об ошибке или null, если слово корректно.
     */
    fun isValidRussianWord(word:String, minLength: Int, maxLength:Int):String?{
        if(word.length < minLength){
            return "Минимальная длина $minLength символов"
        }

        if(word.length > maxLength){
            return "Максимальная длина $maxLength символов"
        }

        val russianWordRegex = Regex("^[А-Яа-я ]+$")
        if(!word.matches(russianWordRegex)){
            return "Допускаются русские буквы и пробел"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданное doubleValue корректным.
     *
     * @param doubleValue Проверяемое значение.
     * @param minValue минимальное значение
     * @param maxValue максимальное значение
     * @return Сообщение об ошибке или null, если doubleValue корректно.
     */
    fun isValidDoubleValue(doubleValue: Double, minValue: Double, maxValue: Double): String? {
        if(doubleValue < minValue){
            return "Минимальное значение: $minValue"
        }

        if(doubleValue > maxValue){
            return "Максимальное значение: $maxValue"
        }

        val stringValue = doubleValue.toString()
        val indexOfDecimal = stringValue.indexOf('.')

        if(indexOfDecimal > 0){
            if(stringValue.length - indexOfDecimal - 1 > 2){
                return "Допустимое количество цифр после запятой = 2"
            }
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка является корректным цветом материала.
     *
     * @param color Проверяемая строка.
     * @return Сообщение об ошибке или null, если цвет материала корректен.
     */
    fun isValidColor(color:String):String?{
        if(color.length < 5){
            return "Минимальная длина: 5 символов"
        }

        if(color.length > 11){
            return "Максимальная длина: 11 символов"
        }

        val colorRegex = Regex("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?);(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?);(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
        if(!color.matches(colorRegex)){
            return "Требуемый формат: R;G;B"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка является корректным размером материала.
     *
     * @param size Проверяемая строка.
     * @return Сообщение об ошибке или null, если размер материала корректен.
     */
    fun isValidSizeOfMaterial(size:String):String?{
        if(size.isEmpty()){
            return "Необходимо ввести"
        }

        if(size.length > 2){
            return "Максимальная длина: 2 символа"
        }

        val sizeRegex = Regex("[A-C][0-9]")
        if(!size.matches(sizeRegex)){
            return "Требуемый формат:формат бумаги + размер"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным городом.
     *
     * @param city Проверяемая строка.
     * @return Сообщение об ошибке или null, если город корректен.
     */
    fun isValidCity(city:String):String?{
        if(city.length < 3){
            return "Минимальная длина: 3 символа"
        }

        if(city.length > 50){
            return "Максимальная длина: 50 символов"
        }

        val cityRegex = Regex("^[А-Яа-я]*(?:[\\s-][А-Яа-я]*)*$")
        if(!city.matches(cityRegex)){
            return "Некорректный ввод города"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректной улицей.
     *
     * @param street Проверяемая строка.
     * @return Сообщение об ошибке или null, если улица корректна.
     */
    fun isValidStreet(street:String):String?{
        if(street.length < 3){
            return "Минимальная длина: 3 символа"
        }

        if(street.length > 50){
            return "Максимальная длина: 50 символов"
        }

        val streetRegex = Regex("[а-яА-Я\\d\\-\\s]+")
        if(!street.matches(streetRegex)){
            return "Некорректный ввод улицы"
        }

        return null
    }

    /**
     * Метод, проверяющий является ли заданная строка корректным номером дома.
     *
     * @param houseNumber Проверяемая строка.
     * @return Сообщение об ошибке или null, если номер дома корректен.
     */
    fun isValidHouseNumber(houseNumber: String):String?{
        if(houseNumber.length == 0){
            return "Необходимо ввести"
        }

        if(houseNumber.length > 50){
            return "Максимальная длина: 50 символов"
        }

        val houseRegex = Regex("^[0-9]+[А-Е]?(/[0-9]+)?$")
        if(!houseNumber.matches(houseRegex)){
            return "Некорректный ввод номера дома"
        }

        return null
    }

}