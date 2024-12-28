package com.example.publishinghousekotlin.dtos

import com.example.publishinghousekotlin.models.PrintingHouse
import java.math.BigDecimal
import java.time.LocalDate

data class BookingSendDTO(
   var id: Long,
   var status: String,
   var startExecution: LocalDate,
   var endExecution: LocalDate?,
   var cost: BigDecimal,
   var printingHouse: PrintingHouse?,
   var productsWithMargin: List<ProductWithEditionDTO>?,
   var idsOfEmployees: List<Long>?
) {
   constructor(): this(0, "ожидание", LocalDate.now(), null, BigDecimal(0), null, null, null)
}