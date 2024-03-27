package com.example.turnback.ui.bars

import android.content.res.Configuration
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.theme.TurnBackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(currentScreen: Screen) {
    val titleId = currentScreen.titleId

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
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Filled.MoreVert,
                    contentDescription = Filled.MoreVert.name
                )
            }
        },
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPreview() {
    TurnBackTheme {
        AppBar(Screen.BottomBarItem.Timer)
    }
}
