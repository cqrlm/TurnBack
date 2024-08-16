package com.example.turnback.ui.bars

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.resources.R
import com.example.timerpreset.TimerEditMode
import com.example.turnback.ui.main.state.MainScreenActions
import com.example.turnback.ui.main.state.MainScreenState
import com.example.ui.common.SingleChoiceDialog
import com.example.ui.theme.ThemeState
import com.example.ui.theme.TurnBackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    state: MainScreenState,
    actions: MainScreenActions
) {
    var showMenu by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    with(state) {
        TopAppBar(
            title = {
                Text(
                    text = when (timerEditMode) {
                        is TimerEditMode.Deletion ->
                            timerEditMode.selectedTimerPresetsCount.toString()

                        is TimerEditMode.Editing -> stringResource(id = R.string.edit)

                        is TimerEditMode.Idle ->
                            currentScreen.titleId?.let { stringResource(id = it) }.orEmpty()
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (timerEditMode !is TimerEditMode.Idle) {
                    IconButton(
                        onClick = {
                            when (timerEditMode) {
                                is TimerEditMode.Deletion -> actions.cancelDeletion()
                                is TimerEditMode.Editing -> actions.cancelEditing()
                                else -> Unit
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = Icons.Filled.Clear.name
                        )
                    }
                }
            },
            actions = {
                Actions(
                    timerEditMode = timerEditMode,
                    isMenuExpanded = showMenu,
                    showMenu = { showMenu = it },
                    showDeleteDialog = { showDeleteDialog = true },
                    showThemeDialog = { showThemeDialog = true },
                    finishEditing = actions.finishEditing
                )
            },
        )

        if (showThemeDialog) {
            ThemeDialog(
                themeState = themeState,
                changeTheme = actions.changeTheme,
                dismiss = { showThemeDialog = false }
            )
        }

        if (showDeleteDialog) {
            DeleteDialog(
                dismiss = { showDeleteDialog = false },
                confirm = {
                    actions.deleteTimerPresets()
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
private fun Actions(
    timerEditMode: TimerEditMode,
    isMenuExpanded: Boolean,
    showMenu: (Boolean) -> Unit,
    showDeleteDialog: () -> Unit,
    showThemeDialog: () -> Unit,
    finishEditing: () -> Unit
) {
    when (timerEditMode) {
        is TimerEditMode.Deletion ->
            if (timerEditMode.selectedTimerPresetsCount != 0) {
                IconButton(onClick = showDeleteDialog) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = Icons.Filled.Delete.name
                    )
                }
            }

        is TimerEditMode.Idle -> {
            IconButton(onClick = { showMenu(true) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = Icons.Filled.MoreVert.name
                )
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { showMenu(false) }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.select_theme)) },
                    onClick = {
                        showMenu(false)
                        showThemeDialog()
                    }
                )
            }
        }

        is TimerEditMode.Editing ->
            IconButton(onClick = finishEditing) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = Icons.Filled.Done.name
                )
            }
    }
}

@Composable
private fun ThemeDialog(
    themeState: ThemeState,
    changeTheme: (ThemeState) -> Unit,
    dismiss: () -> Unit
) {
    SingleChoiceDialog(
        title = stringResource(id = R.string.select_theme),
        dismissTitle = stringResource(id = R.string.cancel),
        dismiss = dismiss,
        options = ThemeState.entries.map { it to stringResource(id = it.stringId) },
        selected = themeState,
        select = { state ->
            changeTheme(state)
            dismiss()
        }
    )
}

@Composable
private fun DeleteDialog(
    dismiss: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = confirm) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = { Text(text = stringResource(id = R.string.delete_dialog_text)) }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarIdlePreview() {
    TurnBackTheme {
        TopBar(
            state = MainScreenState(),
            actions = MainScreenActions()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPresetTimerDeletionPreview() {
    TurnBackTheme {
        TopBar(
            state = MainScreenState(timerEditMode = TimerEditMode.Deletion(10)),
            actions = MainScreenActions()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPresetEditingPreview() {
    TurnBackTheme {
        TopBar(
            state = MainScreenState(timerEditMode = TimerEditMode.Editing()),
            actions = MainScreenActions()
        )
    }
}
