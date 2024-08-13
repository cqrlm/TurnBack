package com.example.ui.common

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> SingleChoiceDialog(
    title: String,
    dismissTitle: String,
    dismiss: () -> Unit,
    options: List<Pair<T, String>>,
    selected: T,
    select: (T) -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        onDismissRequest = dismiss,
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(text = dismissTitle)
            }
        },
        confirmButton = {},
        text = {
            SingleChoiceColumn(
                options = options,
                selected = selected,
                select = select
            )
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SingleChoiceDialogPreview() {
    val options = listOf("First", "Second", "Third")
    var selected by remember { mutableIntStateOf(0) }

    MaterialTheme {
        SingleChoiceDialog(
            title = "Title",
            dismissTitle = "Cancel",
            dismiss = {},
            options = options.mapIndexed { index, option -> index to option },
            selected = selected,
            select = { selection -> selected = selection }
        )
    }
}
