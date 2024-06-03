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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.turnback.AppState
import com.example.turnback.R
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerService
import com.example.turnback.services.timer.TimerState
import com.example.turnback.ui.common.TimePicker
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.ui.timer.state.TimerScreenActions
import com.example.turnback.ui.timer.state.TimerScreenState
import com.example.turnback.utils.formatElapsedTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerScreen(
    timerService: TimerService,
    viewModel: TimerViewModel = hiltViewModel<TimerViewModel, TimerViewModel.Factory>(
        creationCallback = { it.create(timerService.timeFlow, timerService.timerState) }
    )
) {
    val context = LocalContext.current

    with(viewModel) {
        with(timerService) {
            val state by screenState.collectAsState()

            val actions = remember {
                TimerScreenActions(
                    start = { duration -> start(duration, context) },
                    pause = ::pause,
                    resume = ::resume,
                    stop = ::stop,
                    save = ::save,
                    update = ::update,
                    select = ::select,
                    unselect = ::unselect,
                    edit = ::edit,
                    startEditing = ::startEditing
                )
            }

            TimerContent(
                state = state,
                actions = actions
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerContent(
    state: TimerScreenState,
    actions: TimerScreenActions
) {
    with(state) {
        var showCreationDialog by remember { mutableStateOf(false) }

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
                    exit = fadeOut()
                ) {
                    Text(
                        text = timerDuration.formatElapsedTime(),
                        style = Typography.displayLarge
                    )
                }

                AnimatedVisibility(
                    visible = timerState == TimerState.STOP && appState is AppState.Idle,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    TimePicker(modifier = Modifier.padding(top = 30.dp)) { time ->
                        if (time != Duration.ZERO) {
                            actions.start(time)
                        }
                    }
                }

                AnimatedVisibility(
                    visible = timerState == TimerState.STOP,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        timerPresets.forEach { timerPreset ->
                            with(timerPreset) {
                                key(order, selected) {
                                    TimeChip(
                                        selected = selected,
                                        text = duration.formatElapsedTime(),
                                        onClick = {
                                            when (appState) {
                                                is AppState.Editing -> actions.edit(this)

                                                is AppState.Idle -> actions.start(duration)

                                                else ->
                                                    if (selected) {
                                                        actions.unselect(this)
                                                    } else actions.select(this)
                                            }
                                        },
                                        onLongClick = { actions.select(this) }
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = appState is AppState.Idle,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
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
                                TimerState.STOP -> showCreationDialog = true
                            }
                        }

                        if (timerState != TimerState.STOP || timerPresets.isNotEmpty()) {
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
                    }
                }
            }

            if (showCreationDialog) {
                TimerPresetDialog(
                    title = stringResource(id = R.string.new_timer_preset),
                    update = { duration ->
                        showCreationDialog = false

                        if (duration != Duration.ZERO) {
                            actions.save(
                                TimerPreset(
                                    order = timerPresets.size,
                                    duration = duration
                                )
                            )
                        }
                    },
                    dismiss = { showCreationDialog = false }
                )
            }

            if (
                appState is AppState.Editing &&
                appState.editingTimerPreset != TimerPreset.Undefined
            ) {
                with(appState.editingTimerPreset) {
                    TimerPresetDialog(
                        title = stringResource(id = R.string.edit_timer_preset),
                        initialValue = duration,
                        update = { duration ->
                            if (duration != Duration.ZERO) {
                                actions.update(copy(duration = duration))
                            } else actions.startEditing()
                        },
                        dismiss = actions.startEditing
                    )
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
    var isLongClick by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onLongClick?.invoke()
                }
            }
        }
    }

    FilterChip(
        selected = selected,
        onClick = {
            if (!isLongClick) {
                onClick()
            }
        },
        label = { Text(text) },
        interactionSource = interactionSource
    )
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

@Composable
private fun TimerPresetDialog(
    title: String,
    initialValue: Duration? = null,
    update: (Duration) -> Unit,
    dismiss: () -> Unit
) {
    var duration by remember { mutableStateOf(Duration.ZERO) }

    AlertDialog(
        title = { Text(text = title) },
        onDismissRequest = dismiss,
        dismissButton = {
            IconButton(onClick = dismiss) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = Icons.Filled.Clear.name
                )
            }
        },
        confirmButton = {
            IconButton(onClick = { update(duration) }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = Icons.Filled.Done.name
                )
            }
        },
        text = {
            TimePicker(
                initialValue = initialValue ?: Duration.ZERO,
                onDone = { timerDuration -> duration = timerDuration },
                onValueChange = { timerDuration -> duration = timerDuration }
            )
        }
    )
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
