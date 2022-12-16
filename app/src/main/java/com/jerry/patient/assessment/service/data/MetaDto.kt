package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class MetaDto(val lastUpdated: LocalDateTime) : Serializable