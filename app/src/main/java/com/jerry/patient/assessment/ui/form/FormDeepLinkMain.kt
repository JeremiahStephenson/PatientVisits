package com.jerry.patient.assessment.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.ui.common.LocalAppBarTitle
import com.jerry.patient.assessment.ui.common.ErrorIndicator
import com.jerry.patient.assessment.ui.destinations.FormMainDestination
import com.jerry.patient.assessment.ui.destinations.HomeMainDestination
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

/**
 * This probably could be merged with `FormMain.kt`
 * but I like having it separate to keep the logic cleaner
 */
@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "https://jerry.assessment.com/survey/{id}"
        )
    ]
)
@Composable
fun DeepLinkMain(
    id: String,
    navController: DestinationsNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state = viewModel.visitsFlow.collectAsStateWithLifecycle()

    LocalAppBarTitle.current(stringResource(R.string.loading))

    LaunchedEffect(Unit) {
        viewModel.loadVisitsInfo(id)
    }

    LaunchedEffect(state.value.data) {
        state.value.data?.takeIf { state.value.isSuccessful }?.let {
            navController.navigate(
                FormMainDestination(visitInfo = it)
            ) {
                popUpTo(HomeMainDestination.route)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = state.value,
        ) { state ->
            when {
                state.isLoading -> CircularProgressIndicator()
                state.isError -> ErrorIndicator { viewModel.retry() }
            }
        }
    }
}