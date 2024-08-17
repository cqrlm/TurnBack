package com.example.feature.timer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.formatElapsedTime
import com.example.data.model.TimerPreset
import com.example.feature.timer.state.TimerScreenActions
import com.example.feature.timer.state.TimerScreenState
import com.example.resources.R
import com.example.timer.state.TimerServiceActions
import com.example.timer.state.TimerServiceState
import com.example.timer.state.TimerState
import com.example.timerpreset.TimerEditMode
import com.example.ui.common.FadeAnimatedVisibility
import com.example.ui.common.TimePicker
import com.example.ui.theme.TurnBackTheme
import com.example.ui.theme.Typography
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(
    timerServiceState: TimerServiceState,
    timerServiceActions: TimerServiceActions,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state by viewModel.collectState()

    with(timerServiceActions) {
        TimerContent(
            state = state.copy(
                timerDuration = timerServiceState.timerDuration,
                timerState = timerServiceState.timerState
            ),
            actions = viewModel.screenActions.copy(
                start = start,
                pause = pause,
                resume = resume,
                stop = stop
            )
        )
    }
}

@Composable
private fun TimerContent(
    state: TimerScreenState,
    actions: TimerScreenActions
) {
    with(state) {
        var showCreationDialog by remember { mutableStateOf(false) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            FadeAnimatedVisibility(visible = timerState != TimerState.STOP) {
                Text(
                    text = timerDuration.formatElapsedTime(),
                    style = Typography.displayLarge
                )
            }

            FadeAnimatedVisibility(
                visible = timerState == TimerState.STOP && timerEditMode is TimerEditMode.Idle,
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                TimePicker { time ->
                    if (time != Duration.ZERO) {
                        actions.start(time)
                    }
                }
            }

            FadeAnimatedVisibility(visible = timerState == TimerState.STOP) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight(fraction = 0.7f)
                ) {
                    TimerPresetsGrid(
                        timerEditMode = timerEditMode,
                        timerPresets = timerPresets,
                        actions = actions
                    )
                }
            }

            FadeAnimatedVisibility(
                visible = timerEditMode is TimerEditMode.Idle,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                ActionButtons(
                    timerState = timerState,
                    actions = actions,
                    isTimerPresetsNotEmpty = timerPresets.isNotEmpty(),
                    showCreationDialog = { showCreationDialog = true }
                )
            }
        }

        if (showCreationDialog) {
            TimerPresetCreationDialog(
                dismiss = { showCreationDialog = false },
                save = { duration ->
                    actions.save(
                        TimerPreset(
                            order = timerPresets.size,
                            duration = duration
                        )
                    )
                }
            )
        }

        if (timerEditMode is TimerEditMode.Editing) {
            timerEditMode.editingTimerPreset?.let {
                TimerPresetEditingDialog(
                    timerPreset = it,
                    dismiss = actions.startEditing,
                    update = actions.update
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    timerState: TimerState,
    actions: TimerScreenActions,
    isTimerPresetsNotEmpty: Boolean,
    showCreationDialog: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
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
                TimerState.STOP -> showCreationDialog()
            }
        }

        if (timerState != TimerState.STOP || isTimerPresetsNotEmpty) {
            ActionButton(
                icon = when (timerState) {
                    TimerState.START, TimerState.PAUSE ->
                        ImageVector.vectorResource(R.drawable.ic_stop)

                    TimerState.STOP -> Icons.Outlined.Edit
                }
            ) {
                when (timerState) {
                    TimerState.START, TimerState.PAUSE -> actions.stop()
                    TimerState.STOP -> actions.startEditing()
                }
            }
        }

        if (timerState == TimerState.STOP) {
            ActionButton(
                icon = Icons.Outlined.Delete,
                onClick = actions.startDeletion
            )
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, onClick: () -> Unit) {
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
        Surface {
            TimerContent(
                state = TimerScreenState(
                    timerState = TimerState.STOP,
                    timerPresets = List(30) { index ->
                        TimerPreset(index, (30 + index * 2).minutes)
                    }.toMutableStateList()
                ),
                actions = TimerScreenActions()
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPreviewPause() {
    TurnBackTheme {
        Surface {
            TimerContent(
                state = TimerScreenState(
                    timerState = TimerState.PAUSE,
                    timerDuration = 10.seconds
                ),
                actions = TimerScreenActions()
            )
        }
    }
}
