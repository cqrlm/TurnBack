package com.example.turnback

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.utils.formatTime
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration

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
private fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val time = viewModel.timeFlow.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        viewModel.saveTime(time.value)
    }

    MainContent(time.value) {
        viewModel.resetTime()
    }
}

@Composable
fun MainContent(time: Duration, resetTime: () -> Unit) {
    Scaffold { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.TopCenter)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = time.formatTime(),
                    style = Typography.displayLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    contentPadding = PaddingValues(20.dp),
                    onClick = resetTime,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Turn back")
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    TurnBackTheme {
        MainContent(Duration.ZERO) {}
    }
}
