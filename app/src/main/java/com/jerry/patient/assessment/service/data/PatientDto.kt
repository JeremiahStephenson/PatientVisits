package com.jerry.patient.assessment.service.data

import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class PatientDto(
    override val id: String,
    val active: Boolean,
    val name: List<NameDto>,
    val contact: List<ContactDto>,
    val gender: String,
    val birthDate: LocalDate,
    val address: List<AddressDto>
) : Resource, Serializable