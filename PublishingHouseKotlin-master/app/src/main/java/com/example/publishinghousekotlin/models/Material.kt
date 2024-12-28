package com.example.publishinghousekotlin.models

import java.io.Serializable
import java.math.BigDecimal

/**
 * Data-класс,хранящий в себе данные о материале
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property type Тип.
 * @property color Цвет.
 * @property size Размер.
 * @property cost Стоимость.
 * @constructor Создает объект [Material] с заданными значениями или значениями по умолчанию.
 */
data class Material(
    val id: Long,
    var type: String,
    var color: String,
    var size: String,
    var cost: BigDecimal
): Serializable {
    constructor(): this(0, "","","",BigDecimal(0))
}