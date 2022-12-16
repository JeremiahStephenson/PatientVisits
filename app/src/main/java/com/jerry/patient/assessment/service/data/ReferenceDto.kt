package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ReferenceDto(val reference: String) : Serializable