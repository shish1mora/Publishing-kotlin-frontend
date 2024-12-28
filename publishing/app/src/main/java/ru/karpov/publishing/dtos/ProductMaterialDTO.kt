package ru.karpov.publishing.dtos

import ru.karpov.publishing.models.Material
import java.io.Serializable


data class ProductMaterialDTO(
    val material: Material,
    val countMaterials: Int
){
}