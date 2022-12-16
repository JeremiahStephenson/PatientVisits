package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class AddressDto(
    val use: String,
    val line: List<String>
) : Serializable