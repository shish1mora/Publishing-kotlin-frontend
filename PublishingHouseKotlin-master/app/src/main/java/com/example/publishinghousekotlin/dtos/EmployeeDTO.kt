package com.example.publishinghousekotlin.dtos

import java.io.Serializable
import java.time.LocalDate
/**
 * Data-класс,представляющий собой объект принимаемого с сервера сотрудника.
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
 * @property photo Фотография в виде Base64String
 * @property birthday Дата рождения.
 * @constructor Создает объект [EmployeeDTO] с заданными значениями или значениями по умолчанию.
 */
data class EmployeeDTO(
    val id: Long,
    var surname: String,
    var name: String ,
    var patronymic:String,
    var phone:String,
    var email:String,
    var post: String,
    var pathToImage: String,
    var photo: String,
    var birthday: LocalDate,
): Serializable
{
    constructor(): this(0, "", "", "", "", "", "","", "", LocalDate.now())

}