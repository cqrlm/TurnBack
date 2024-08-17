package com.example.feature.timer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.formatElapsedTime
import com.example.data.model.TimerPreset
import com.example.feature.timer.state.TimerScreenActions
import com.example.timerpreset.TimerEditMode
import com.example.ui.common.DraggableItem
import com.example.ui.common.dragContainer
import com.example.ui.common.rememberGridDragDropState
import com.example.ui.theme.TurnBackTheme
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerPresetsGrid(
    timerEditMode: TimerEditMode,
    timerPresets: SnapshotStateList<TimerPreset>,
    actions: TimerScreenActions
) {
    val gridState = rememberLazyGridState()

    if (timerEditMode is TimerEditMode.Editing) {
        DraggableTimerPresetsGrid(
            gridState = gridState,
            timerPresets = timerPresets,
            onClick = { timerPreset -> actions.edit(timerPreset) },
            swap = actions.swap
        )
    } else {
        NonDraggableTimerPresetsGrid(
            gridState = gridState,
            timerPresets = timerPresets,
            onClick = { timerPreset ->
                when (timerEditMode) {
                    is TimerEditMode.Idle -> actions.start(timerPreset.duration)
                    is TimerEditMode.Deletion -> actions.select(timerPreset)
                    else -> Unit
                }
            },
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPresetsGridPreview() {
    TurnBackTheme {
        Surface {
            TimerPresetsGrid(
                timerEditMode = TimerEditMode.Idle,
                timerPresets = remember {
                    mutableStateListOf(
                        TimerPreset(0, 30.seconds),
                        TimerPreset(1, 2.minutes),
                        TimerPreset(2, 15.minutes),
                        TimerPreset(3, 1.hours)
                    )
                },
                actions = TimerScreenActions()
            )
        }
    }
}
