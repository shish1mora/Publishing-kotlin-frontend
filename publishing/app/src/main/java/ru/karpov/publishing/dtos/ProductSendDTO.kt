package ru.karpov.publishing.dtos

import ru.karpov.publishing.models.TypeProduct
import java.io.Serializable
import java.math.BigDecimal


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