package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class DiagnosisDto(
    override val id: String,
    val meta: MetaDto,
    val status: String,
    val code: CodeDto,
    val appointment: ReferenceDto
) : Resource, Serializable