package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class AppointmentDto(
    override val id: String,
    val status: String,
    val type: List<TypeDto>,
    val subject: ReferenceDto,
    val actor: ReferenceDto,
    val period: PeriodDto
) : Resource, Serializable