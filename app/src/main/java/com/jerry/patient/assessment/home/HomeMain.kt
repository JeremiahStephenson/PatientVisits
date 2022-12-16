package com.jerry.patient.assessment.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.core.*
import com.jerry.patient.assessment.destinations.FormMainDestination
import com.jerry.patient.assessment.service.Data
import com.jerry.patient.assessment.service.data.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeMain(
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.visitsFlow.collectAsState(initial = Data.idle())

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
                items = state.value.data?.entries ?: emptyList(),
                key = { it.id }
            ) { item ->
                when (item) {
                    is PatientDto -> PatientInfo(item)
                    is DoctorDto -> DoctorInfo(item)
                    is DiagnosisDto -> DiagnosisInfo(item)
                    is AppointmentDto -> AppointmentInfo(item)
                }
            }
            state.value.data?.let {
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        onClick = {
                            navController.navigate(
                                FormMainDestination(patientInfo = it)
                            )
                        }) {
                        Text(stringResource(R.string.evaluate_visit))
                    }
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
            append(data.code.coding.joinToString(", ") { it.name })
        })
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
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
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