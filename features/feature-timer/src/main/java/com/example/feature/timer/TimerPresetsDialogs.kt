package com.example.feature.timer

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.data.model.TimerPreset
import com.example.resources.R
import com.example.ui.common.TimePicker
import com.example.ui.theme.TurnBackTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun TimerPresetCreationDialog(
    dismiss: () -> Unit,
    save: (Duration) -> Unit
) {
    TimerPresetDialog(
        title = stringResource(id = R.string.new_timer_preset),
        update = { duration ->
            if (duration != Duration.ZERO) {
                save(duration)
            }

            dismiss()
        },
        dismiss = dismiss
    )
}

@Composable
internal fun TimerPresetEditingDialog(
    timerPreset: TimerPreset,
    dismiss: () -> Unit,
    update: (TimerPreset) -> Unit
) {
    with(timerPreset) {
        TimerPresetDialog(
            title = stringResource(id = R.string.edit_timer_preset),
            initialValue = duration,
            update = { duration ->
                if (duration != Duration.ZERO) {
                    update(timerPreset.copy(duration = duration))
                }

                dismiss()
            },
            dismiss = dismiss
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
private fun TimerPresetCreationDialogPreview() {
    TurnBackTheme {
        Surface {
            TimerPresetCreationDialog(
                dismiss = {},
                save = {}
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPresetEditingDialogPreview() {
    TurnBackTheme {
        Surface {
            TimerPresetEditingDialog(
                timerPreset = TimerPreset(
                    order = 0,
                    duration = 30.seconds
                ),
                dismiss = {},
                update = {}
            )
        }
    }
}
