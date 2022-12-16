package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class VisitsDto(
    val resourceType: String,
    val id: String,
    val timestamp: LocalDateTime,
    val entry: List<ResourceDto>
) : Serializable {
    val entries: List<Resource> get() = entry.map { it.resource }
}