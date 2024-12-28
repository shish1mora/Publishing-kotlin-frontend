package com.example.publishinghousekotlin.models

import com.example.publishinghousekotlin.dtos.EmployeeDTO
import java.io.Serializable
import java.time.LocalDate


/**
 * Data-класс,хранящий в себе данные о сотруднике
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property surname Фамилия.
 * @property name Имя.
 * @property patronymic Отчество.
 * @property phone Номер телефона.
 * @property email Электронная почта.
 * @property post Должность.
 * @property pathToImage Путь до фотографии на сервере.
 * @property birthday Дата рождения.
 * @constructor Создает объект [Employee] с заданными значениями или значениями по умолчанию.
 */
data class Employee(
    val id: Long,
    var surname: String,
    var name: String,
    var patronymic:String,
    var phone:String,
    var email:String,
    var post: String,
    var pathToImage: String,
    var birthday: LocalDate
):Serializable {
    constructor(): this(0, "", "", "", "", "", "", "", LocalDate.now())
}