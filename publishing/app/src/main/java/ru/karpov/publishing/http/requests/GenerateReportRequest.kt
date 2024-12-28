package ru.karpov.publishing.http.requests

import java.time.LocalDate

data class GenerateReportRequest(
    val userId: Long,
    val userRole: String,
    val startPeriod: LocalDate,
    val endPeriod: LocalDate
) {
}