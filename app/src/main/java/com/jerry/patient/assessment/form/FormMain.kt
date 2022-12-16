package com.jerry.patient.assessment.form

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.core.*
import com.jerry.patient.assessment.service.data.VisitsDto
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun FormMain(
    visitInfo: VisitsDto,
    navController: DestinationsNavigator,
    viewModel: FormViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.feedback))

    var showDialog by remember { mutableStateOf(false) }
    BackHandler {
        showDialog = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState()
        var forwardEnabled by rememberSaveable {
            mutableStateOf(false)
        }
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            state = pagerState,
            count = 4,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> Rating(visitInfo, rememberCurrentPage(pagerState, page)) {
                    forwardEnabled = it
                }
                1 -> Understanding(visitInfo, rememberCurrentPage(pagerState, page)) {
                    forwardEnabled = it
                }
                2 -> Feelings(rememberCurrentPage(pagerState, page)) {
                    forwardEnabled = it
                }
                3 -> Summary()
            }
        }
        val scope = rememberCoroutineScope()
        ButtonRow(
            pagerState = pagerState,
            forwardEnabled = forwardEnabled,
            onBackward = { scope.moveBackward(pagerState) },
            onForward = { scope.moveForward(pagerState) },
            onSubmit = {
                // todo
            }
        )
        ProgressIndicator(
            page = pagerState.currentPage,
            count = pagerState.pageCount
        )
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
fun Rating(
    visitInfo: VisitsDto,
    isCurrentPage: Boolean,
    onEnableOrDisableContinue: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_1_1,
                visitInfo.patient.name.givenName,
                visitInfo.doctor.name.givenName
            )
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.form_question_1_2)
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.form_question_1_3)
        )
        var rating by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(isCurrentPage) {
            onEnableOrDisableContinue(rating > 0)
        }

        RatingBar(
            rating = rating,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            rating = it
            onEnableOrDisableContinue(rating > 0)
        }
    }
}

@Composable
fun Understanding(
    visitInfo: VisitsDto,
    isCurrentPage: Boolean,
    onEnableOrDisableContinue: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(R.string.thank_you)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_2_1,
                visitInfo.diagnosis.code.diagnosis
            )
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_2_2,
                visitInfo.doctor.name.givenName
            )
        )
        var positive by rememberSaveable { mutableStateOf<Boolean?>(null) }
        LaunchedEffect(isCurrentPage) {
            onEnableOrDisableContinue(positive != null)
        }
        LikeButtons(positive = positive) {
            positive = it
            onEnableOrDisableContinue(true)
        }
    }
}

@Composable
fun Feelings(
    isCurrentPage: Boolean,
    onEnableOrDisableContinue: (Boolean) -> Unit
) {
    LaunchedEffect(isCurrentPage) {
        onEnableOrDisableContinue(false)
    }
}

@Composable
fun Summary() {

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ButtonRow(
    pagerState: PagerState,
    forwardEnabled: Boolean = false,
    onForward: () -> Unit,
    onBackward: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
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
                drawableRes = R.drawable.ic_baseline_arrow_back_24
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
                    drawableRes = R.drawable.ic_baseline_arrow_forward_24
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
    onClick: () -> Unit
) {
    Icon(
        modifier = Modifier
            .unboundClickable(enabled) { onClick() }
            .padding(16.dp),
        painter = painterResource(drawableRes),
        contentDescription = null,
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
        modifier = Modifier.fillMaxWidth(),
        progress = progress
    )
}

@Composable
private fun LikeButtons(
    positive: Boolean?,
    onSelected: (Boolean) -> Unit
) {
    Row(modifier = Modifier.padding(24.dp)) {
        LikeButton(
            selected = positive == true,
            tint = Color.Green
        ) {
            onSelected(true)
        }
        Spacer(modifier = Modifier.width(50.dp))
        LikeButton(
            selected = positive == false,
            tint = Color.Red,
            up = false
        ) {
            onSelected(false)
        }
    }
}

@Composable
private fun LikeButton(
    selected: Boolean,
    tint: Color,
    up: Boolean = true,
    onSelected: () -> Unit
) {
    Icon(
        modifier = Modifier
            .rotate(if (up) 0F else 180F)
            .clip(CircleShape)
            .clickable {
                onSelected()
            }
            .run {
                when (selected) {
                    true -> background(tint.copy(alpha = 0.3F))
                    else -> this
                }
            }
            .padding(16.dp),
        imageVector = Icons.Default.ThumbUp,
        contentDescription = null,
        tint = tint
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = goBack
            ) {
                Text(stringResource(R.string.yes))
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = dismiss
            ) {
                Text(stringResource(R.string.no))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun CoroutineScope.moveForward(
    pagerState: PagerState,
) {
    if (pagerState.currentPage < pagerState.pageCount) {
        launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun CoroutineScope.moveBackward(
    pagerState: PagerState
) {
    if (pagerState.currentPage > 0) {
        launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun rememberCurrentPage(
    pagerState: PagerState,
    page: Int
): Boolean {
    return remember { derivedStateOf { pagerState.currentPage == page && pagerState.currentPageOffset == 0F } }.value
}