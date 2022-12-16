package com.jerry.patient.assessment.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.compose.LocalAppBarTitle
import com.jerry.patient.assessment.compose.MediumButton
import com.jerry.patient.assessment.destinations.FormMainDestination
import com.jerry.patient.assessment.service.data.*
import com.jerry.patient.assessment.util.READABLE_TIME_PATTERN_PARSER
import com.jerry.patient.assessment.util.READBLE_DATE_TIME_PATTERN_PARSER
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalLifecycleComposeApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeMain(
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.visitsFlow.collectAsStateWithLifecycle()

    LocalAppBarTitle.current(stringResource(R.string.patient_visits))

    LaunchedEffect(Unit) {
        viewModel.loadVisitsInfo(PATIENT_INFO_ID)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(
                items = state.value.data?.visits?.entries ?: emptyList(),
                key = { it.id }
            ) { item ->
                when (item) {
                    is PatientDto -> PatientInfo(item)
                    is DoctorDto -> DoctorInfo(item)
                    is DiagnosisDto -> DiagnosisInfo(item)
                    is AppointmentDto -> AppointmentInfo(item)
                }
            }
            state.value.data?.feedback?.let {
                item {
                    FeedbackInfo(feedback = it)
                }
            }
            state.value.data?.let {
                item {
                    MediumButton(
                        text = stringResource(
                            when (state.value.data?.feedback) {
                                null -> R.string.evaluate_visit
                                else -> R.string.edit_feedback
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        onClick = {
                            navController.navigate(
                                FormMainDestination(visitInfo = it)
                            )
                        })
                }
            }
        }

        AnimatedContent(
            targetState = state.value,
        ) { state ->
            when {
                state.isLoading -> CircularProgressIndicator()
                state.isError -> ErrorIndicator {
                    viewModel.retry()
                }
            }
        }
    }
}

@Composable
private fun PatientInfo(data: PatientDto) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = buildLargeTitleAnnotatedString(
            stringResource(R.string.name)
        ) {
            append(data.name.readableName)
        })
}

@Composable
private fun DoctorInfo(data: DoctorDto) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = buildLargeTitleAnnotatedString(
            stringResource(R.string.doctor)
        ) {
            append(data.name.readableName)
        })
}

@Composable
private fun AppointmentInfo(data: AppointmentDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(text = buildLargeTitleAnnotatedString(
            stringResource(R.string.date_of_visit)
        ) {
            append(
                "${data.period.start.format(READBLE_DATE_TIME_PATTERN_PARSER)} - ${
                    when (data.period.start.toLocalDate() == data.period.end.toLocalDate()) {
                        true -> data.period.end.format(READABLE_TIME_PATTERN_PARSER)
                        else -> data.period.end.format(READBLE_DATE_TIME_PATTERN_PARSER)
                    }
                }"
            )
        })
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = buildLargeTitleAnnotatedString(stringResource(R.string.type_of_visit)) {
                append(data.type.joinToString(", ") { it.text })
            }
        )
    }
}

@Composable
private fun DiagnosisInfo(data: DiagnosisDto) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = buildLargeTitleAnnotatedString(stringResource(R.string.diagnosis)) {
            append(data.code.diagnosis)
        })
}

@Composable
private fun FeedbackInfo(feedback: Feedback) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = buildLargeTitleAnnotatedString(stringResource(R.string.patient_feedback)) {
            append(feedback.feedback)
        }
    )
}

@Composable
private fun ErrorIndicator(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_error_outline_24),
            contentDescription = null
        )
        Text(text = stringResource(R.string.error_message))
        MediumButton(
            text = stringResource(R.string.retry),
            onClick = onRetry
        )
    }
}

@Composable
private fun buildLargeTitleAnnotatedString(
    title: String,
    content: AnnotatedString.Builder.() -> Unit
) = buildAnnotatedString {
    withStyle(MaterialTheme.typography.titleLarge.toSpanStyle()) {
        append("$title: ")
    }
    content()
}

private val List<NameDto>.readableName
    get() = joinToString(" ") {
        it.given.joinToString(" ", postfix = " ${it.family}") { name -> name }
    }

private const val PATIENT_INFO_ID = "0c3151bd-1cbf-4d64-b04d-cd9187a4c6e0"