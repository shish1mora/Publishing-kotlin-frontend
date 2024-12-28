package ru.karpov.publishing.dtos


data class CountProductsInBookingDTO(
    val booking: BookingSimpleAcceptDTO,
    val edition: Int
){

}