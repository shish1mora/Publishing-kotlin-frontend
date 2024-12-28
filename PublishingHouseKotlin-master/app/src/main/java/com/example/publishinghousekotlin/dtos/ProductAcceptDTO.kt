package com.example.publishinghousekotlin.dtos

import com.example.publishinghousekotlin.models.TypeProduct
import java.io.Serializable
import java.math.BigDecimal

/**
 * Data-класс,представляющий собой объект принимаемой с сервера продукции.
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property name Наименование.
 * @property username Имя пользователя,являющимся владельцем продукции.
 * @property userEmail Электронная почта пользователя.
 * @property cost Стоимость.
 * @property typeProduct Тип продукции (может быть null).
 * @property productMaterialDTOS Список объектов [ProductMaterialDTO], представляющих собой материал и его количество в продукции (может быть null).
 * @property countProductsInBookingDTOS Список объектов [CountProductsInBookingDTO], представляющих собой заказ и количество продукции в нем(может быть null).
 * @property photos Список строк, представляющих собой фотографии продукции в виде Base64String (может быть null).
 * @constructor Создает объект [ProductAcceptDTO] с заданными значениями или значениями по умолчанию.
 */
data class ProductAcceptDTO(
    val id: Long,
    val name: String,
    val username: String,
    val userEmail: String,
    val cost: BigDecimal,
    val typeProduct: TypeProduct?,
    val productMaterialDTOS: List<ProductMaterialDTO>?,
    val countProductsInBookingDTOS: List<CountProductsInBookingDTO>?,
    val photos: List<String>?
):Serializable{
    constructor(): this(0, "", "", "", BigDecimal(0), null, null, null,null)
}