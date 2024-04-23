package com.example.turnback.ui.timer

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.turnback.R
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.utils.formatElapsedTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(viewModel: TimerViewModel = hiltViewModel()) {
    val timerDuration = viewModel.timeFlow.collectAsState()
    val timerState = viewModel.timerState.collectAsState()

    val times = TIMES

    TimerContent(
        timerState = timerState.value,
        timerDuration = timerDuration.value,
        times = times,
        start = viewModel::start,
        pause = viewModel::pause,
        resume = viewModel::resume,
        stop = viewModel::stop
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerContent(
    timerState: TimerState,
    timerDuration: Duration,
    times: List<TimerPreset>,
    start: (Duration) -> Unit,
    pause: () -> Unit,
    resume: () -> Unit,
    stop: () -> Unit
) {
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            AnimatedVisibility(
                visible = timerState != TimerState.STOP,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = timerDuration.formatElapsedTime(),
                    style = Typography.displayLarge
                )
            }

            AnimatedVisibility(
                visible = timerState == TimerState.STOP,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    times.forEach { time ->
                        Chip(time.duration.formatElapsedTime()) {
                            start(time.duration)
                        }
                    }
                }
            }


            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                ActionButton(
                    icon = when (timerState) {
                        TimerState.START -> ImageVector.vectorResource(R.drawable.ic_pause)
                        TimerState.PAUSE -> ImageVector.vectorResource(R.drawable.ic_start)
                        TimerState.STOP -> Icons.Outlined.Add
                    }
                ) {
                    when (timerState) {
                        TimerState.START -> pause()
                        TimerState.PAUSE -> resume()
                        TimerState.STOP -> Unit
                    }
                }

                ActionButton(
                    icon = when (timerState) {
                        TimerState.START, TimerState.PAUSE ->
                            ImageVector.vectorResource(R.drawable.ic_stop)

                        TimerState.STOP -> Icons.Outlined.Edit
                    }
                ) {
                    when (timerState) {
                        TimerState.START, TimerState.PAUSE -> stop()
                        TimerState.STOP -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String, onClick: () -> Unit) {
    SuggestionChip(
        label = { Text(text) },
        onClick = onClick
    )
}

@Composable
private fun ActionButton(icon: ImageVector, onClick: () -> Unit = {}) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name
        )
    }
}

private val TIMES = listOf(
    TimerPreset(0, 30.seconds),
    TimerPreset(1, 2.minutes),
    TimerPreset(2, 3.minutes),
    TimerPreset(3, 5.minutes),
    TimerPreset(4, 10.minutes),
    TimerPreset(5, 15.minutes),
    TimerPreset(6, 20.minutes),
    TimerPreset(7, 30.minutes),
    TimerPreset(8, 1.hours)
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPreviewStop() {
    TurnBackTheme {
        TimerContent(
            timerState = TimerState.STOP,
            timerDuration = 0.seconds,
            times = TIMES,
            start = {},
            pause = {},
            resume = {},
            stop = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPreviewPause() {
    TurnBackTheme {
        TimerContent(
            timerState = TimerState.PAUSE,
            timerDuration = 10.seconds,
            times = TIMES,
            start = {},
            pause = {},
            resume = {},
            stop = {}
        )
    }
}
