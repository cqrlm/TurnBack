package com.example.turnback.ui.timer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.turnback.ui.theme.TurnBackTheme

@Composable
fun TimerScreen() {
    TimerContent()
}

@Composable
private fun TimerContent() {
    Text(text = "Timer")
}

@Preview
@Composable
private fun TimerPreview() {
    TurnBackTheme {
        TimerContent()
    }
}
