package com.jerry.patient.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.jerry.patient.assessment.core.LocalAppBarTitle
import com.jerry.patient.assessment.core.unboundClickable
import com.jerry.patient.assessment.ui.theme.AssessmentTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AssessmentTheme {
                Content()
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun Content() {
    val engine = rememberAnimatedNavHostEngine()
    val navController = engine.rememberNavController()
    var title by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(
        LocalAppBarTitle provides { title = it },
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.navigationBars,
            topBar = {
                val showBackArrow by remember(navController.appCurrentDestinationAsState().value) {
                    derivedStateOf { navController.previousBackStackEntry != null }
                }
                Toolbar(
                    showBackArrow = { showBackArrow },
                    onBack = { navController.popBackStack() },
                    getTitle = { title.orEmpty() }
                )
            }) { innerPadding ->
            DestinationsNavHost(
                modifier = Modifier.padding(innerPadding),
                engine = engine,
                navGraph = NavGraphs.root,
                navController = navController,
                startRoute = NavGraphs.root.startRoute,
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)
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
                        .padding(16.dp)
                        .unboundClickable {
                            onBack()
                        },
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
