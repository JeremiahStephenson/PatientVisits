package com.jerry.patient.assessment.service.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class ResourceType {
    @Json(name = "Patient")
    PATIENT,
    @Json(name = "Doctor")
    DOCTOR,
    @Json(name = "Appointment")
    APPOINTMENT,
    @Json(name = "Diagnosis")
    DIAGNOSIS
}