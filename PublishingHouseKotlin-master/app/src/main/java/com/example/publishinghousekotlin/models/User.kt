package com.example.publishinghousekotlin.models

import java.io.Serializable

/**
 * Data-класс,хранящий в себе данные о пользователе
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property name Наименование.
 * @property phone Номер телефона.
 * @property email Электронная почта.
 * @property role Роль.
 * @constructor Создает объект [User] с заданными значениями или значениями по умолчанию.
 */
data class User(

    val id: Long,
    val name:String,
    val phone: String,
    val email: String,
    val role: String

): Serializable{
    companion object{
        private const val serialVersionUID = 528283900914260698L
    }
}