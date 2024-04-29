package com.example.turnback.ui.bars

import android.content.res.Configuration
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.turnback.R
import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.common.SingleChoiceDialog
import com.example.turnback.ui.theme.ThemeState
import com.example.turnback.ui.theme.TurnBackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: Screen,
    themeState: ThemeState,
    changeTheme: (ThemeState) -> Unit
) {
    val titleId = currentScreen.titleId
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (titleId != null) {
                Text(
                    text = stringResource(id = titleId),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Filled.MoreVert,
                    contentDescription = Filled.MoreVert.name
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.select_theme)) },
                    onClick = {
                        showMenu = false
                        showDialog = true
                    }
                )
            }
        },
    )

    if (showDialog) {
        ThemeDialog(
            themeState = themeState,
            changeTheme = changeTheme,
            dismiss = { showDialog = false }
        )
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
        dismiss = dismiss,
        options = ThemeState.entries.map { it to stringResource(id = it.stringId) },
        selected = themeState,
        select = { state ->
            changeTheme(state)
            dismiss()
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPreview() {
    TurnBackTheme {
        AppBar(
            currentScreen = Screen.BottomBarItem.Timer,
            themeState = ThemeState.SYSTEM
        ) {}
    }
}
