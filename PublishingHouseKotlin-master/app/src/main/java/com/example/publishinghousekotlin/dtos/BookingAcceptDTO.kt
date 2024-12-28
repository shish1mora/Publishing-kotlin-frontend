package com.example.publishinghousekotlin.dtos

import com.example.publishinghousekotlin.models.PrintingHouse
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class BookingAcceptDTO(
    val id: Long,
    val status: String,
    val startExecution: LocalDate,
    val endExecution: LocalDate?,
    val cost: BigDecimal,
    val printingHouse: PrintingHouse?,
    val products: List<ProductSimpleAcceptDTO>?,
    val employees: List<EmployeeDTO>?
):Serializable {
    constructor(): this(0, "",  LocalDate.now(), null, BigDecimal(0), null, null, null)
}