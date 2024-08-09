package com.example.turnback.ui.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.timer.TimerService
import com.example.turnback.ui.bars.BottomNavBar
import com.example.turnback.ui.bars.TopBar
import com.example.turnback.ui.main.state.MainScreenActions
import com.example.turnback.ui.main.state.MainScreenState
import com.example.turnback.ui.stopwatch.StopwatchScreen
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.timer.TimerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var timerService: TimerService? by mutableStateOf(null)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            timerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(timerService)

            NotificationCheck()
        }
    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this, TimerService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(connection)
        timerService = null
        super.onStop()
    }
}

@Composable
private fun MainScreen(
    timerService: TimerService?,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

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
                        timerServiceActions = timerServiceActions
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
                BottomNavBar(
                    navController = navController,
                    changeScreen = actions.changeScreen
                )
            }
        ) { paddingValues -> content(paddingValues) }
    }
}

@Composable
private fun NotificationCheck() {
    val context = LocalContext.current

    val hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        if (!hasNotificationPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
