package com.example.publishinghousekotlin.models


import java.io.Serializable


/**
 * Data-класс,хранящий в себе данные о типе продукции
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property type Тип.
 * @property margin Наценка в %.
 * @constructor Создает объект [TypeProduct] с заданными значениями или значениями по умолчанию.
 */
data class TypeProduct(

    val id: Long,
    var type: String,
    var margin: Double
): Serializable {

    constructor() : this(0, "", 0.0)

}