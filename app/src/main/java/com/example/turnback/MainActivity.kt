package com.example.turnback

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.bars.AppBar
import com.example.turnback.ui.bars.BottomNavBar
import com.example.turnback.ui.stopwatch.StopwatchScreen
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TurnBackTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val navController = rememberNavController()
    var currentScreen: Screen by remember {
        mutableStateOf(Screen.BottomBarItem.Timer)
    }

    Scaffold(
        topBar = { AppBar(currentScreen) },
        bottomBar = { BottomNavBar(navController) { screen -> currentScreen = screen } }
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    TurnBackTheme {
        MainScreen()
    }
}
