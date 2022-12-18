package com.jerry.patient.assessment

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.jerry.patient.assessment.extensions.unboundClickable
import com.jerry.patient.assessment.ui.NavGraphs
import com.jerry.patient.assessment.ui.appCurrentDestinationAsState
import com.jerry.patient.assessment.ui.common.LocalAppBarTitle
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalLayoutApi::class,
)
@Composable
fun Content(
    onBackPressed: () -> Unit
) {
    val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING
    )
    val navController = engine.rememberNavController()

    var title by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(
        LocalAppBarTitle provides { title = it },
    ) {
        Scaffold(
            modifier = Modifier.run {
                when (LocalConfiguration.current.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> this
                    else -> imePadding()
                }
            },
            topBar = {
                val showBackArrow by remember(navController.appCurrentDestinationAsState().value) {
                    derivedStateOf { navController.previousBackStackEntry != null }
                }
                Toolbar(
                    showBackArrow = { showBackArrow },
                    onBack = onBackPressed,
                    getTitle = { title.orEmpty() }
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            DestinationsNavHost(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .systemBarsPadding(),
                engine = engine,
                navGraph = NavGraphs.root,
                navController = navController,
                startRoute = NavGraphs.root.startRoute,
            )
        }
    }
}

@Composable
fun Toolbar(
    showBackArrow: () -> Boolean,
    onBack: () -> Unit,
    getTitle: () -> String
) {
    val topAppBarElementColor = MaterialTheme.colorScheme.onPrimary
    val appBarContainerColor = MaterialTheme.colorScheme.primary
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        navigationIcon = {
            AnimatedVisibility(
                visible = showBackArrow(),
                enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                exit = shrinkOut(shrinkTowards = Alignment.Center) + fadeOut()
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .unboundClickable {
                            onBack()
                        }
                        .padding(8.dp),
                    painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.animateContentSize(),
                text = getTitle()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appBarContainerColor,
            scrolledContainerColor = appBarContainerColor,
            navigationIconContentColor = topAppBarElementColor,
            titleContentColor = topAppBarElementColor,
            actionIconContentColor = topAppBarElementColor,
        )
    )
}