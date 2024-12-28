package com.example.publishinghousekotlin.http.responses

/**
 * Класс,хранящий в себе код состояния и сообщение, полученные от сервера.
 * @author Климачков Даниил
 * @since 1.0.0
 * @property code Код состояния.
 * @property message Сообщение.
 */
data class MessageResponse (
    val code:Int,
    val message: String
)