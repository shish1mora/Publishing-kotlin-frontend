package ru.karpov.publishing.models

import java.io.Serializable


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