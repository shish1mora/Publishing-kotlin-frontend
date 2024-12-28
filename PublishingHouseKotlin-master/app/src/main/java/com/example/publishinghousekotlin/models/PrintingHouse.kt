package com.example.publishinghousekotlin.models

import java.io.Serializable

/**
 * Data-класс,хранящий в себе данные о типографии
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property name Наименование.
 * @property phone Номер телефона.
 * @property email Электронная почта.
 * @property state Субъект РФ.
 * @property city Город.
 * @property street Улица.
 * @property house Номер дома.
 * @constructor Создает объект [PrintingHouse] с заданными значениями или значениями по умолчанию.
 */
data class PrintingHouse(
    val id: Long,
    var name: String,
    var phone: String,
    var email: String,
    var state: String,
    var city: String,
    var street: String,
    var house: String
): Serializable {
    constructor(): this(0, "", "", "", "", "", "", "")
}