package com.jerry.patient.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.jerry.patient.assessment.compose.LocalAppBarTitle
import com.jerry.patient.assessment.extensions.unboundClickable
import com.jerry.patient.assessment.compose.theme.AssessmentTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AssessmentTheme {
                Content { onBackPressedDispatcher.onBackPressed() }
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
fun Content(onBackPressed: () -> Unit) {
    val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING
    )
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
                    onBack = onBackPressed,
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
    ExperimentalMaterial3Api::class
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
