package com.example.turnback.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.bars.AppBar
import com.example.turnback.ui.bars.BottomNavBar
import com.example.turnback.ui.main.state.MainScreenActions
import com.example.turnback.ui.main.state.MainScreenState
import com.example.turnback.ui.stopwatch.StopwatchScreen
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { MainScreen() }
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    with(viewModel) {
        val navController = rememberNavController()

        val state by screenState.collectAsState()

        val actions = remember {
            MainScreenActions(
                changeScreen = ::changeScreen,
                changeTheme = ::setThemeState,
                clearSelection = ::clearSelection,
                deleteTimerPresets = ::deleteTimerPresets,
                finishEditing = ::finishEditing
            )
        }

        MainContent(
            navController = navController,
            state = state,
            actions = actions
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.BottomBarItem.Timer.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                composable(Screen.BottomBarItem.Timer.route) { TimerScreen() }
                composable(Screen.BottomBarItem.Stopwatch.route) { StopwatchScreen() }
            }
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
    with(state) {
        TurnBackTheme(themeState.isDarkTheme()) {
            Scaffold(
                topBar = {
                    AppBar(
                        appState = appState,
                        themeState = themeState,
                        changeTheme = actions.changeTheme,
                        clearSelection = actions.clearSelection,
                        deleteTimerPresets = actions.deleteTimerPresets,
                        finishEditing = actions.finishEditing
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        changeScreen = actions.changeScreen
                    )
                }
            ) { paddingValues -> content(paddingValues) }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    MainContent(
        state = MainScreenState(),
        actions = MainScreenActions.Default
    ) {}
}
