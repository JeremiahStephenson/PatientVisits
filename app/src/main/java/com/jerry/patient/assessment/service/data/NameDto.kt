package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class NameDto(
    val text: String?,
    val family: String,
    val given: List<String>
) : Serializable