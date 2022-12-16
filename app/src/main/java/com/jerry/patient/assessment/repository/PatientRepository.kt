package com.jerry.patient.assessment.repository

import com.jerry.patient.assessment.service.data.VisitsDto

interface PatientRepository {
    suspend fun loadVisitsInfo(patientId: String): VisitsDto
}