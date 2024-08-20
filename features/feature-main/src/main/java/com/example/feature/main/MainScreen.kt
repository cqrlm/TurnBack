package com.example.feature.main

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature.main.bars.BottomBar
import com.example.feature.main.bars.TopBar
import com.example.feature.main.state.MainScreenActions
import com.example.feature.main.state.MainScreenState
import com.example.feature.stopwatch.StopwatchScreen
import com.example.feature.timer.TimerScreen
import com.example.navigation.Screen
import com.example.navigation.isSelected
import com.example.navigation.navigate
import com.example.timer.TimerService
import com.example.ui.theme.TurnBackTheme

@Composable
fun MainScreen(
    activityClassName: Class<out Activity>,
    timerService: TimerService?,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val state by viewModel.collectState()

    MainContent(
        navController = navController,
        state = state,
        actions = viewModel.screenActions
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.START_DESTINATION.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            composable(Screen.BottomBarItem.Timer.route) {
                timerService?.run {
                    val timerServiceState by timerServiceState.collectAsState()

                    TimerScreen(
                        timerServiceState = timerServiceState,
                        timerServiceActions = actions(context, activityClassName)
                    )
                }
            }

            composable(Screen.BottomBarItem.Stopwatch.route) { StopwatchScreen() }
        }
    }
}

@Composable
private fun MainContent(
    navController: NavHostController = rememberNavController(),
    state: MainScreenState,
    actions: MainScreenActions,
    content: @Composable (PaddingValues) -> Unit
) {
    TurnBackTheme(state.themeState.isDarkTheme()) {
        Scaffold(
            topBar = {
                TopBar(
                    state = state,
                    actions = actions
                )
            },
            bottomBar = {
                BottomBar(
                    selected = { screen -> navController.isSelected(screen) },
                    onClick = { screen ->
                        navController.navigate(screen)
                        actions.changeScreen(screen)
                    }
                )
            }
        ) { paddingValues -> content(paddingValues) }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    MainContent(
        state = MainScreenState(),
        actions = MainScreenActions()
    ) {}
}
