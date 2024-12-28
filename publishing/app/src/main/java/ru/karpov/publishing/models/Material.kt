package ru.karpov.publishing.models

import java.io.Serializable
import java.math.BigDecimal


data class Material(
    val id: Long,
    var type: String,
    var color: String,
    var size: String,
    var cost: BigDecimal
): Serializable {
    constructor(): this(0, "","","",BigDecimal(0))
}