package com.jerry.patient.assessment.service

import com.jerry.patient.assessment.service.data.VisitsDto
import retrofit2.http.GET

interface VisitsAPI {
    @GET("patient-feedback-raw-data.json")
    suspend fun getVisitsInfo(): VisitsDto
}