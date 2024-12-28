package ru.karpov.publishing.dtos

import java.math.BigDecimal


data class BookingSimpleAcceptDTO(
    val id: Long,
    val cost: BigDecimal
){

}