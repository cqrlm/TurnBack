package com.example.turnback.ui.timer

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.turnback.R
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.ui.timer.state.TimerScreenActions
import com.example.turnback.ui.timer.state.TimerScreenState
import com.example.turnback.utils.formatElapsedTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(viewModel: TimerViewModel = hiltViewModel()) {
    with(viewModel) {
        val state by screenState.collectAsState()

        val actions = remember {
            TimerScreenActions(
                start = ::start,
                pause = ::pause,
                resume = ::resume,
                stop = ::stop,
                save = ::save,
                delete = ::delete
            )
        }

        TimerContent(
            state = state,
            actions = actions
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerContent(state: TimerScreenState, actions: TimerScreenActions) {
    with(state) {
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
                    val selectedTimes = remember {
                        mutableStateListOf<TimerPreset>()
                    }

                    val isEditMode by remember {
                        derivedStateOf { selectedTimes.isNotEmpty() }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        timerPresets.forEach { timerPreset ->
                            val selected by remember {
                                derivedStateOf { timerPreset in selectedTimes }
                            }

                            TimeChip(
                                selected = selected,
                                text = timerPreset.duration.formatElapsedTime(),
                                onClick = {
                                    when {
                                        !isEditMode -> actions.start(timerPreset.duration)
                                        timerPreset in selectedTimes -> selectedTimes.remove(timerPreset)
                                        else -> selectedTimes.add(timerPreset)
                                    }
                                },
                                onLongClick = if (!isEditMode) {
                                    { selectedTimes.add(timerPreset) }
                                } else null
                            )
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
                            TimerState.START -> actions.pause()

                            TimerState.PAUSE -> actions.resume()

                            TimerState.STOP ->
                                // TODO: Add implementation of timer preset addition
                                actions.save(
                                    TimerPreset(
                                        order = timerPresets.size,
                                        duration = 30.seconds + timerPresets.size.seconds
                                    )
                                )
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
                            TimerState.START, TimerState.PAUSE -> actions.stop()
                            // TODO : Add implementation of timer presets deletion
                            TimerState.STOP -> actions.delete(timerPresets)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?
) {
    val interactionSource = remember { MutableInteractionSource() }
    val viewConfiguration = LocalViewConfiguration.current

    LaunchedEffect(interactionSource) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onLongClick?.invoke()
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        onClick()
                    }
                }
            }
        }
    }

    FilterChip(
        selected = selected,
        onClick = {},
        label = { Text(text) },
        interactionSource = interactionSource
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPreviewStop() {
    TurnBackTheme {
        TimerContent(
            state = TimerScreenState(
                timerState = TimerState.STOP,
                timerPresets = listOf(
                    TimerPreset(0, 30.seconds),
                    TimerPreset(1, 2.minutes),
                    TimerPreset(2, 15.minutes),
                    TimerPreset(3, 1.hours)
                ).toMutableStateList()
            ),
            actions = TimerScreenActions.Default
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPreviewPause() {
    TurnBackTheme {
        TimerContent(
            state = TimerScreenState(
                timerState = TimerState.PAUSE,
                timerDuration = 10.seconds
            ),
            actions = TimerScreenActions.Default
        )
    }
}
