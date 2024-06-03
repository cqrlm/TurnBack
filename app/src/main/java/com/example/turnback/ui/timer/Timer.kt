package com.example.turnback.ui.timer

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.turnback.R
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerService
import com.example.turnback.services.timer.TimerState
import com.example.turnback.services.timer.preset.TimerEditMode
import com.example.turnback.ui.common.DraggableItem
import com.example.turnback.ui.common.TimePicker
import com.example.turnback.ui.common.dragContainer
import com.example.turnback.ui.common.rememberGridDragDropState
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.ui.timer.state.TimerScreenActions
import com.example.turnback.ui.timer.state.TimerScreenState
import com.example.turnback.utils.formatElapsedTime
import kotlin.time.Duration
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
                    edit = ::edit,
                    startEditing = ::startEditing,
                    startDeletion = ::startDeletion,
                    swap = ::swap
                )
            }

            TimerContent(
                state = state,
                actions = actions
            )
        }
    }
}

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
                    visible = timerState == TimerState.STOP && timerEditMode is TimerEditMode.Idle,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    TimePicker { time ->
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
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxHeight(fraction = 0.7f)
                    ) {
                        TimerPresetsGrid(
                            timerPresets = timerPresets,
                            isDraggable = timerEditMode is TimerEditMode.Editing,
                            onClick = { timerPreset ->
                                when (timerEditMode) {
                                    is TimerEditMode.Editing -> actions.edit(timerPreset)
                                    is TimerEditMode.Idle -> actions.start(timerPreset.duration)
                                    is TimerEditMode.Deletion -> actions.select(timerPreset)
                                }
                            },
                            swap = actions.swap
                        )
                    }
                }

                AnimatedVisibility(
                    visible = timerEditMode is TimerEditMode.Idle,
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

                        if (timerState == TimerState.STOP) {
                            ActionButton(
                                icon = Icons.Outlined.Delete,
                                onClick = actions.startDeletion
                            )
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
                timerEditMode is TimerEditMode.Editing &&
                timerEditMode.editingTimerPreset != null
            ) {
                with(timerEditMode.editingTimerPreset) {
                    TimerPresetDialog(
                        title = stringResource(id = R.string.edit_timer_preset),
                        initialValue = duration,
                        update = { duration ->
                            if (duration != Duration.ZERO) {
                                actions.update(copy(duration = duration))
                            }

                            actions.startEditing()
                        },
                        dismiss = actions.startEditing
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerPresetsGrid(
    timerPresets: SnapshotStateList<TimerPreset>,
    isDraggable: Boolean,
    onClick: (TimerPreset) -> Unit,
    swap: (Int, Int) -> Unit
) {
    val gridState = rememberLazyGridState()

    if (isDraggable) {
        DraggableTimerPresetsGrid(
            gridState = gridState,
            timerPresets = timerPresets,
            onClick = onClick,
            swap = swap
        )
    } else {
        NonDraggableTimerPresetsGrid(
            gridState = gridState,
            timerPresets = timerPresets,
            onClick = onClick
        )
    }
}

@Composable
private fun DraggableTimerPresetsGrid(
    gridState: LazyGridState,
    timerPresets: SnapshotStateList<TimerPreset>,
    onClick: (TimerPreset) -> Unit,
    swap: (Int, Int) -> Unit
) {
    val dragDropState = rememberGridDragDropState(gridState) { fromIndex, toIndex ->
        swap(fromIndex, toIndex)
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.dragContainer(dragDropState)
    ) {
        itemsIndexed(
            items = timerPresets,
            key = { _, timerPreset -> timerPreset.hashCode() }
        ) { index, timerPreset ->
            DraggableItem(dragDropState, index) { _ ->
                TimeChip(
                    timerPreset = timerPreset,
                    onClick = { onClick(timerPreset) }
                )
            }
        }
    }
}

@Composable
private fun NonDraggableTimerPresetsGrid(
    gridState: LazyGridState,
    timerPresets: SnapshotStateList<TimerPreset>,
    onClick: (TimerPreset) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = timerPresets,
            key = { timerPreset -> timerPreset.hashCode() }
        ) { timerPreset ->
            TimeChip(
                timerPreset = timerPreset,
                onClick = { onClick(timerPreset) }
            )
        }
    }
}

@Composable
private fun TimeChip(
    timerPreset: TimerPreset,
    onClick: () -> Unit
) {
    with(timerPreset) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = {
                Text(
                    text = duration.formatElapsedTime(),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
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
                timerPresets = List(30) { index ->
                    TimerPreset(index, (30 + index * 2).minutes)
                }.toMutableStateList()
            ),
            actions = TimerScreenActions()
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
            actions = TimerScreenActions()
        )
    }
}
