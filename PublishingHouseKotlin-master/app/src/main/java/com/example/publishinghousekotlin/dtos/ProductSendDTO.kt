package com.example.publishinghousekotlin.dtos

import com.example.publishinghousekotlin.models.TypeProduct
import java.io.Serializable
import java.math.BigDecimal

/**
 * Data-класс,представляющий собой объект отправляемой на сервер продукции.
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property userId Уникальный идентификатор пользователя, являющимся создателем продукции.
 * @property name Наименование.
 * @property cost Стоимость.
 * @property typeProduct Тип продукции (может быть null).
 * @property productMaterialDTOS Список объектов [ProductMaterialDTO], представляющих собой материал и его количество в продукции (может быть null).
 * @constructor Создает объект [ProductSendDTO] с заданными значениями или значениями по умолчанию.
 */
data class ProductSendDTO(
    var id: Long,
    var userId: Long,
    var name: String,
    var cost: BigDecimal,
    var typeProduct: TypeProduct?,
    var productMaterialDTOS: List<ProductMaterialDTO>?,
) {
    constructor():this(0,0, "", BigDecimal(0), null,null)
}