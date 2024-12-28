package ru.karpov.publishing.dtos

import java.io.Serializable
import java.time.LocalDate

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