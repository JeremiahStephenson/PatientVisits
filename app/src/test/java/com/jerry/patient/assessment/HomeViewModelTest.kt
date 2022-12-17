package com.jerry.patient.assessment

import app.cash.turbine.test
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.repository.PatientRepository
import com.jerry.patient.assessment.service.data.VisitsDto
import com.jerry.patient.assessment.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class HomeViewModelTest {

    private val patientRepository = mockk<PatientRepository>()

    private val feedbackFlow = MutableStateFlow<Feedback?>(null)

    @get:Rule
    val testRule = MainTestRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(patientRepository)
        every { patientRepository.feedbackFlow(any()) } returns feedbackFlow
    }

    @Test
    fun testFlowSuccess() = runTest {
        coEvery { patientRepository.loadVisitsInfo(any()) } returns DUMMY_VISITS

        val feedback = Feedback()
        feedbackFlow.value = feedback

        assert(viewModel.visitsFlow.value.isIdle)
        viewModel.loadVisitsInfo("id")

        viewModel.visitsFlow.test {
            val item = awaitItem()
            assert(item.isSuccessful)
            assertEquals(item.data?.feedback, feedback)
            assertEquals(item.data?.visits, DUMMY_VISITS)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testFlowFail() = runTest {
        coEvery { patientRepository.loadVisitsInfo(any()) } coAnswers {
            throw Throwable("Testing")
        }

        assert(viewModel.visitsFlow.value.isIdle)
        viewModel.loadVisitsInfo("id")

        viewModel.visitsFlow.test {
            val item = awaitItem()
            assert(item.isError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        val DUMMY_VISITS = VisitsDto(
            resourceType = "Bundle",
            id = "id",
            timestamp = LocalDateTime.now(),
            entry = emptyList()
        )
    }
}