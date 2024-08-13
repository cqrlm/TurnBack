package com.example.turnback.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.theme.TurnBackTheme

@Composable
fun <T> SingleChoiceColumn(
    options: List<Pair<T, String>>,
    selected: T,
    select: (T) -> Unit
) {
    Column {
        options.forEach { (option, text) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { select(option) }
            ) {
                RadioButton(
                    selected = option == selected,
                    onClick = { select(option) }
                )

                Text(text = text)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleChoiceColumnPreview() {
    val options = listOf("First", "Second", "Third")
    var selected by remember { mutableIntStateOf(0) }

    TurnBackTheme {
        SingleChoiceColumn(
            options = options.mapIndexed { index, option -> index to option },
            selected = selected
        ) { selection -> selected = selection }
    }
}
