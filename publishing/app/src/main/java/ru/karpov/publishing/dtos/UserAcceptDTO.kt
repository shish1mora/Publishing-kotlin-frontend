package ru.karpov.publishing.dtos

data class UserAcceptDTO(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val products: List<ProductSimpleAcceptDTO>?,
    val bookings: List<BookingSimpleAcceptDTO>?
) {
}