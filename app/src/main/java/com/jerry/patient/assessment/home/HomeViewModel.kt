package com.jerry.patient.assessment.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.service.Data
import com.jerry.patient.assessment.service.data.VisitsDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val handle: SavedStateHandle,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val patientInfoId = MutableStateFlow<String?>(null)

    val visitsFlow = patientInfoId.transformLatest { id ->
        if (id == null) {
            emit(Data.done(null))
            return@transformLatest
        }
        emit(Data.loading())
        try {
            val data = patientRepository.loadVisitsInfo(id)
            emit(Data.done(data))
        } catch (t: Throwable) {
            emit(Data.error<VisitsDto>(t))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Data.idle()
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