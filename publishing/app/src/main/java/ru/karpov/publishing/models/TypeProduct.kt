package ru.karpov.publishing.models


import java.io.Serializable



data class TypeProduct(

    val id: Long,
    var type: String,
    var margin: Double
): Serializable {

    constructor() : this(0, "", 0.0)

}