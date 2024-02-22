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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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
    var time by remember {
        mutableStateOf(Duration.ZERO)
    }

    val formattedTime by remember(time) {
        mutableStateOf(
            time.toComponents { hours, minutes, seconds, _ ->
                "%02d:%02d:%02d".format(hours, minutes, seconds)
            }
        )
    }

    var resetTime by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(resetTime, time) {
        if (resetTime) {
            time = Duration.ZERO
            resetTime = false
        } else {
            val interval = 1.seconds
            delay(interval)
            time += interval
        }
    }

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
                    text = formattedTime,
                    style = Typography.displayLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    contentPadding = PaddingValues(20.dp),
                    onClick = { resetTime = true },
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
        MainScreen()
    }
}
