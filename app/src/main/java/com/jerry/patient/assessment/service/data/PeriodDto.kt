package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class PeriodDto(
    val start: LocalDateTime,
    val end: LocalDateTime
) : Serializable