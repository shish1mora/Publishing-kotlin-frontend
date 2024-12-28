package com.example.publishinghousekotlin.dtos

import java.math.BigDecimal

data class ProductSimpleAcceptDTO(
    val id: Long,
    val name: String,
    val username: String,
    val edition: Int,
    val photo: String,
    val cost: BigDecimal
) {


}