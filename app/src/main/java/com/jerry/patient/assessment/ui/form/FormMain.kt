package com.jerry.patient.assessment.ui.form

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.extensions.givenName
import com.jerry.patient.assessment.extensions.unboundClickable
import com.jerry.patient.assessment.service.VisitsData
import com.jerry.patient.assessment.service.data.VisitsDto
import com.jerry.patient.assessment.ui.common.FadeAnimatedVisibility
import com.jerry.patient.assessment.ui.common.LocalAppBarTitle
import com.jerry.patient.assessment.ui.common.MediumButton
import com.jerry.patient.assessment.ui.common.theme.PatientTheme
import com.jerry.patient.assessment.ui.common.theme.ThemePreviews
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun FormMain(
    visitInfo: VisitsData,
    navController: DestinationsNavigator,
    viewModel: FormViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.feedback))

    val uiState = getFormUiState(viewModel, visitInfo.feedback)

    var showDialog by remember { mutableStateOf(false) }
    BackHandler(uiState.feedBackChanged) {
        showDialog = true
    }

    val context = LocalContext.current
    val feedbackSuccessText = stringResource(R.string.feedback_saved)
    LaunchedEffect(Unit) {
        viewModel.feedbackSaved.collectLatest {
            Toast.makeText(context, feedbackSuccessText, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    FormContent(
        visitInfo = visitInfo.visits,
        uiState = uiState,
        onRated = { viewModel.saveRating(it) },
        onUnderstanding = { viewModel.saveUnderstanding(it) },
        onFeedback = { viewModel.saveFeedback(it) },
        onImageSelected = { viewModel.saveImage(it) }
    ) {
        viewModel.submit()
    }

    if (showDialog) {
        AreYouSureDialog(
            dismiss = { showDialog = false },
            goBack = {
                showDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
private fun FormContent(
    visitInfo: VisitsDto,
    uiState: FormUiState,
    onRated: (Int) -> Unit,
    onUnderstanding: (Boolean) -> Unit,
    onFeedback: (String?) -> Unit,
    onImageSelected: (Uri?) -> Unit,
    submit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState()
        // We don't want the forward button to be enabled until the user
        // has answered the question.
        // This allows the sub composables to control that.
        var forwardEnabled by rememberSaveable { mutableStateOf(false) }
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            state = pagerState,
            count = 4,
            userScrollEnabled = false
        ) { page ->
            val patientName = visitInfo.patient.name.givenName
            val doctorName = visitInfo.doctor.name.givenName
            val diagnosis = visitInfo.diagnosis.code.diagnosis.lowercase()
            when (page) {
                0 -> Rating(
                    patientName = patientName,
                    doctorName = doctorName,
                    isCurrentPage = rememberCurrentPage(pagerState, page),
                    onRated = onRated,
                    getRating = { uiState.feedBack?.rating ?: 0 },
                    onEnableOrDisableContinue = { forwardEnabled = it }
                )
                1 -> Understanding(
                    diagnosis,
                    doctorName,
                    isCurrentPage = rememberCurrentPage(pagerState, page),
                    onUnderstanding = onUnderstanding,
                    getUnderstanding = { uiState.feedBack?.understanding },
                    onEnableOrDisableContinue = { forwardEnabled = it }
                )
                2 -> Feedback(
                    diagnosis = diagnosis,
                    isCurrentPage = rememberCurrentPage(pagerState, page),
                    onFeedback = onFeedback,
                    getFeedback = { uiState.feedBack?.feedback },
                    onEnableOrDisableContinue = { forwardEnabled = it }
                )
                3 -> Summary(
                    results = uiState.feedBack,
                    onImageSelected = onImageSelected
                )
            }
        }
        val scope = rememberCoroutineScope()
        ButtonRow(
            pagerState = pagerState,
            forwardEnabled = forwardEnabled,
            onBackward = { scope.moveBackward(pagerState) },
            onForward = { scope.moveForward(pagerState) },
            onSubmit = submit
        )
        ProgressIndicator(
            page = pagerState.currentPage,
            count = pagerState.pageCount
        )
    }
}

@Composable
private fun ButtonRow(
    pagerState: PagerState,
    forwardEnabled: Boolean = false,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    onSubmit: () -> Unit
) {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2F)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val showBackArrow by remember {
            derivedStateOf {
                pagerState.currentPage > 0
            }
        }
        FadeAnimatedVisibility(visible = showBackArrow) {
            ProgressButton(
                drawableRes = R.drawable.ic_baseline_arrow_back_24,
                contentDescription = stringResource(R.string.back)
            ) {
                onBackward()
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        Box(
            contentAlignment = Alignment.CenterEnd
        ) {
            val showForwardArrow by remember {
                derivedStateOf {
                    pagerState.currentPage < (pagerState.pageCount - 1)
                }
            }
            FadeAnimatedVisibility(visible = showForwardArrow) {
                ProgressButton(
                    enabled = forwardEnabled,
                    drawableRes = R.drawable.ic_baseline_arrow_forward_24,
                    contentDescription = stringResource(R.string.forward)
                ) {
                    onForward()
                }
            }
            val showSubmitButton by remember {
                derivedStateOf { pagerState.currentPage == (pagerState.pageCount - 1) }
            }
            FadeAnimatedVisibility(visible = showSubmitButton) {
                MediumButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.submit)
                ) {
                    onSubmit()
                }
            }
        }
    }
}

@Composable
private fun ProgressButton(
    @DrawableRes drawableRes: Int,
    enabled: Boolean = true,
    contentDescription: String,
    onClick: () -> Unit
) {
    Icon(
        modifier = Modifier
            .unboundClickable(enabled) { onClick() }
            .padding(16.dp),
        painter = painterResource(drawableRes),
        contentDescription = contentDescription,
        tint = when (enabled) {
            true -> LocalContentColor.current
            else -> LocalContentColor.current.copy(alpha = 0.4F)
        }
    )
}

@Composable
private fun ProgressIndicator(
    page: Int,
    count: Int
) {
    val progress by animateFloatAsState(targetValue = (page + 1) / count.toFloat())
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp),
        progress = progress
    )
}

@Composable
private fun AreYouSureDialog(
    dismiss: () -> Unit,
    goBack: () -> Unit
) {
    Dialog(onDismissRequest = dismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.are_you_sure),
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = goBack
            ) {
                Text(text = stringResource(R.string.yes))
            }
            MediumButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(R.string.no),
                onClick = dismiss
            )
        }
    }
}

private fun CoroutineScope.moveForward(
    pagerState: PagerState,
) {
    if (pagerState.currentPage < pagerState.pageCount) {
        launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

private fun CoroutineScope.moveBackward(
    pagerState: PagerState
) {
    if (pagerState.currentPage > 0) {
        launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }
}

@Composable
private fun rememberCurrentPage(
    pagerState: PagerState,
    page: Int
): Boolean {
    return remember { derivedStateOf { pagerState.currentPage == page && pagerState.currentPageOffset == 0F } }.value
}

@Composable
private fun getFormUiState(
    viewModel: FormViewModel,
    feedback: Feedback?
): FormUiState {
    val feedBackChangedState = viewModel.feedbackHasChanged.collectAsStateWithLifecycle()
    val feedBackState = viewModel.feedbackFlow.collectAsStateWithLifecycle(feedback)
    return FormUiState(feedBackChangedState, feedBackState)
}

@ThemePreviews
@Composable
private fun AreYouSureDialogPreview() {
    PatientTheme {
        AreYouSureDialog(dismiss = {}) {}
    }
}

@Preview
@Composable
private fun PreviewButtonRow() {
    PatientTheme {
        ButtonRow(
            pagerState = rememberPagerState(1),
            forwardEnabled = true,
            onForward = {},
            onBackward = {}
        ) {}
    }
}

