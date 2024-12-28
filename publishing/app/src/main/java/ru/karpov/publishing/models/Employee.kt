package ru.karpov.publishing.models


import java.io.Serializable
import java.time.LocalDate


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