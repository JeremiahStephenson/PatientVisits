package com.jerry.patient.assessment.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.service.DataResource
import com.jerry.patient.assessment.service.VisitsData
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val patientRepository: PatientRepository,
) : ViewModel() {

    private val patientInfoId = MutableStateFlow<String?>(null)

    val visitsFlow = patientInfoId.transformLatest { id ->
        if (id == null) {
            emit(DataResource.done(null))
            return@transformLatest
        }
        emit(DataResource.loading())
        try {
            val data = patientRepository.loadVisitsInfo(id)
            emitAll(patientRepository.feedbackFlow(data.id).transformLatest { value ->
                emit(DataResource.done(VisitsData(data, value)))
            })
        } catch (t: Throwable) {
            emit(DataResource.error<VisitsData>(t))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DataResource.idle()
    )

    fun loadVisitsInfo(id: String) {
        patientInfoId.value = id
    }

    fun retry() {
        val id = patientInfoId.value
        patientInfoId.value = null
        patientInfoId.value = id
    }
}