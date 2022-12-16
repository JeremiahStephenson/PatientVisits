package com.jerry.patient.assessment.repository

import com.jerry.patient.assessment.service.VisitsAPI
import com.jerry.patient.assessment.service.data.VisitsDto

class PatientRepositoryImpl(
    private val visitsAPI: VisitsAPI
) : PatientRepository {

    override suspend fun loadVisitsInfo(patientId: String): VisitsDto {
        return visitsAPI.getVisitsInfo()
    }
}