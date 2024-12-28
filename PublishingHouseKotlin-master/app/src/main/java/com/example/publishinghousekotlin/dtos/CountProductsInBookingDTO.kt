package com.example.publishinghousekotlin.dtos

/**
 * Data-класс,хранящий в себе данные о количестве продукции в заказе.
 * @author Денис
 * @since 1.0.0
 * @property booking Заказ.
 * @property edition Количество продукции.
 */
data class CountProductsInBookingDTO(
    val booking: BookingSimpleAcceptDTO,
    val edition: Int
){

}