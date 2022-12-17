package com.jerry.patient.assessment

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.service.VisitsData
import com.jerry.patient.assessment.ui.form.FormViewModel
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FormViewModelTest {

    private val handle = SavedStateHandle()

    private lateinit var viewModel: FormViewModel

    private val patientRepository = mockk<PatientRepository>()

    private val uri = mockk<Uri>(relaxed = true)

    @get:Rule
    val testRule = MainTestRule()

    @Before
    fun setUp() {
        handle["visitInfo"] = VisitsData(
            HomeViewModelTest.DUMMY_VISITS,
            DUMMY_FEEDBACK
        )
        viewModel = FormViewModel(
            handle,
            testRule.contextProvider,
            patientRepository
        )
        coJustRun { patientRepository.saveFeedback(any()) }
    }

    @Test
    fun testUpdating() = runTest {
        handle[FormViewModel.ARG_FEEDBACK] = DUMMY_FEEDBACK

        // nothing has changed yet
        assert(!viewModel.feedbackHasChanged.value)

        // Make sure the updates are reflected
        val newRating = 6
        assertEquals(handle.feedback?.rating, DUMMY_FEEDBACK.rating)
        viewModel.saveRating(newRating)
        assertEquals(handle.feedback?.rating, newRating)

        val changedFeedback = "Changed Feedback"
        assertEquals(handle.feedback?.feedback, DUMMY_FEEDBACK.feedback)
        viewModel.saveFeedback(changedFeedback)
        assertEquals(handle.feedback?.feedback, changedFeedback)

        val changedUnderstanding = false
        assertEquals(handle.feedback?.understanding, DUMMY_FEEDBACK.understanding)
        viewModel.saveUnderstanding(changedUnderstanding)
        assertEquals(handle.feedback?.understanding, changedUnderstanding)

        val newImage = uri
        assertEquals(handle.feedback?.image, DUMMY_FEEDBACK.image)
        viewModel.saveImage(uri)
        assertEquals(handle.feedback?.image, newImage.toString())

        // Now save and ensure we got a signal back
        viewModel.feedbackSaved.test {
            viewModel.submit()
            val item = awaitItem()
            assertNotNull(item)
        }
    }

    private val SavedStateHandle.feedback get() = get<Feedback>(FormViewModel.ARG_FEEDBACK)

    companion object {
        private val DUMMY_FEEDBACK =
            Feedback("id", 8, true, "Test", null)
    }
}