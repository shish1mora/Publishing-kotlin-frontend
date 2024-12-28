package com.example.publishinghousekotlin.http.requests

/**
 * Класс,хранящий в себе данные для авторизации пользователя
 * @author Денис
 * @since 1.0.0
 * @property email Электронная почта.
 * @property password Пароль.
 */
data class LoginRequest(

    private val email:String,
    private val password: String
)