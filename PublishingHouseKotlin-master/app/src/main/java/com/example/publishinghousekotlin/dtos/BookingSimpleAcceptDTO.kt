package com.example.publishinghousekotlin.dtos

import java.math.BigDecimal

/**
 * Data-класс,хранящий в себе неполные данные о заказе.
 * @author Денис
 * @since 1.0.0
 * @property id Уникальный идентификатор.
 * @property cost Стоимость.
 */
data class BookingSimpleAcceptDTO(
    val id: Long,
    val cost: BigDecimal
){

}