package com.example.turnback.ui.timer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.turnback.ui.theme.TurnBackTheme

@Composable
fun TimerScreen() {
    TimerContent()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerContent() {
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            val time = listOf(
                "00:30", "02:00", "03:00", "05:00", "10:00", "15:00", "20:00",
                "30:00", "01:00:00"
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                time.forEach { text ->
                    Chip(text)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                ActionButton(Icons.Outlined.Add)
                ActionButton(Icons.Outlined.Edit)
            }
        }
    }
}

@Composable
private fun Chip(text: String, onClick: () -> Unit = {}) {
    SuggestionChip(
        label = { Text(text) },
        onClick = onClick
    )
}

@Composable
private fun ActionButton(icon: ImageVector, onClick: () -> Unit = {}) {
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
private fun TimerPreview() {
    TurnBackTheme {
        TimerContent()
    }
}
