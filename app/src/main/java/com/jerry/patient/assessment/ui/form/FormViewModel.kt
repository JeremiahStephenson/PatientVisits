package com.jerry.patient.assessment.ui.form

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.ui.destinations.FormMainDestination
import com.jerry.patient.assessment.util.CoroutineContextProvider
import com.jerry.patient.assessment.util.SavedHandle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting

class FormViewModel(
    private val handle: SavedStateHandle,
    private val cc: CoroutineContextProvider,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val originalFeedback =
        FormMainDestination.argsFrom(handle).visitInfo.feedback ?: Feedback()

    // Let's the UI know that the data has been saved
    private val _feedBackSaved =
        MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    val feedbackSaved = _feedBackSaved.asSharedFlow()

    val feedbackFlow = handle.getStateFlow<Feedback?>(ARG_FEEDBACK, null)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            originalFeedback
        )

    private var feedback by SavedHandle<Feedback?>(handle, ARG_FEEDBACK, originalFeedback)

    private val _feedbackHasChanged = MutableStateFlow(false)
    val feedbackHasChanged = _feedbackHasChanged.asStateFlow()

    init {
        checkIfChanged()
    }

    fun saveRating(rating: Int) {
        feedback = feedback?.copy(rating = rating)
        checkIfChanged()
    }

    fun saveUnderstanding(understanding: Boolean) {
        feedback = feedback?.copy(understanding = understanding)
        checkIfChanged()
    }

    fun saveFeedback(feedback: String?) {
        this.feedback = this.feedback?.copy(feedback = feedback)
        checkIfChanged()
    }

    fun saveImage(uri: Uri?) {
        this.feedback = this.feedback?.copy(image = uri?.toString())
        checkIfChanged()
    }

    fun submit() {
        feedback?.let {
            viewModelScope.launch(cc.io) {
                patientRepository.saveFeedback(
                    it.apply {
                        it.visitId = FormMainDestination.argsFrom(handle).visitInfo.visits.id
                    })
                // This should save really quickly but we still
                // need for the UI to wait anyway before moving on
                _feedBackSaved.tryEmit(Unit)
            }
        }
    }

    private fun checkIfChanged() {
        _feedbackHasChanged.value = originalFeedback != feedback
    }

    companion object {
        @VisibleForTesting
        const val ARG_FEEDBACK = "ARG_FEEDBACK"
    }
}