package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CodingDto(
    val system: String,
    val code: String,
    val name: String
) : Serializable