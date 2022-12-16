package com.jerry.patient.assessment.service.adapters

import com.jerry.patient.assessment.service.data.*
import com.squareup.moshi.*

class ResourceAdapter {
    // Even though we don't need to serialize this data we
    // have to have this here or else it won't deserialize
    @ToJson
    @Suppress("UNUSED_PARAMETER")
    fun toJson(data: Resource): String? = null

    @FromJson
    fun fromJson(
        reader: JsonReader,
        patientAdapter: JsonAdapter<PatientDto>,
        doctorAdapter: JsonAdapter<DoctorDto>,
        appointmentAdapter: JsonAdapter<AppointmentDto>,
        diagnosisAdapter: JsonAdapter<DiagnosisDto>,
        unknownAdapter: JsonAdapter<UnknownDto>,
        resourceTypeAdapter: JsonAdapter<ResourceType>
    ) : Resource? {
        @Suppress("UNCHECKED_CAST")
        val value = reader.readJsonValue() as Map<String, Any>
        return try {
            when (resourceTypeAdapter.fromJsonValue(value["resourceType"] as String)) {
                ResourceType.PATIENT -> patientAdapter.fromJsonValue(value)
                ResourceType.DOCTOR -> doctorAdapter.fromJsonValue(value)
                ResourceType.APPOINTMENT -> appointmentAdapter.fromJsonValue(value)
                ResourceType.DIAGNOSIS -> diagnosisAdapter.fromJsonValue(value)
                else -> null
            }
        } catch (e: JsonDataException) {
            unknownAdapter.fromJsonValue(value)
        }
    }
}