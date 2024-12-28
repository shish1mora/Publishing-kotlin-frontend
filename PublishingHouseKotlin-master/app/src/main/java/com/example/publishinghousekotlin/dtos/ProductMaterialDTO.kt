package com.example.publishinghousekotlin.dtos

import com.example.publishinghousekotlin.models.Material
import java.io.Serializable

/**
 * Data-класс,хранящий в себе данные о материале и его количестве в продукции.
 * @author Денис
 * @since 1.0.0
 * @property material Материал.
 * @property countMaterials Количество материала.
 */
data class ProductMaterialDTO(
    val material: Material,
    val countMaterials: Int
){
}