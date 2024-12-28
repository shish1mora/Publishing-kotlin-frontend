package com.example.publishinghousekotlin.http.requests

/**
 * Класс,хранящий в себе данные для регистрации пользователя
 * @author Денис
 * @since 1.0.0
 * @property name Наименование
 * @property phone Номер телефона
 * @property email Электронная почта.
 * @property password Пароль.
 * @property role Роль.
 */
data class RegisterRequest(

    private val name:String,
    private val phone:String,
    private val email:String,
    private val password:String,
    private val role: String
)