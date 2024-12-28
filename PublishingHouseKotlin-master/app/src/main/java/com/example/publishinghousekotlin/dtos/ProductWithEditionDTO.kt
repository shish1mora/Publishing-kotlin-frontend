package com.example.publishinghousekotlin.dtos

import java.math.BigDecimal

data class ProductWithEditionDTO(
    var id: Long,
    var name: String?,
    var cost: BigDecimal?,
    var edition: Int
) {
    constructor(): this(0, null , null, 0)
}