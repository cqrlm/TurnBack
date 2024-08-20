package com.example.feature.main.bars

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.navigation.Screen
import com.example.ui.theme.TurnBackTheme

@Composable
fun BottomBar(selected: (Screen) -> Boolean, onClick: (Screen) -> Unit) {
    val items = remember {
        listOf(Screen.BottomBarItem.Timer, Screen.BottomBarItem.Stopwatch)
    }

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selected(screen),
                onClick = { onClick(screen) },
                label = { Text(text = stringResource(id = screen.titleId)) },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconId),
                        contentDescription = screen.route
                    )
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomBarPreview() {
    TurnBackTheme {
        BottomBar(selected = { screen -> screen is Screen.BottomBarItem.Timer }) {}
    }
}
