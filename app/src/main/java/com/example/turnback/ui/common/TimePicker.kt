package com.example.turnback.ui.common

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import com.example.turnback.ui.common.Time.Companion.toTime
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    initialValue: Duration = Duration.ZERO,
    textStyle: TextStyle = Typography.displayMedium,
    onValueChange: ((Duration) -> Unit)? = null,
    onDone: (Duration) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var time: Time by remember { mutableStateOf(initialValue.toTime()) }

    val textValue by remember(time) { mutableStateOf(time.toString()) }

    val customTextSelectionColors = remember {
        TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.Transparent
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = TextFieldValue(
                text = textValue,
                selection = TextRange(textValue.length)
            ),
            textStyle = textStyle.merge(TextStyle(LocalContentColor.current)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone(time.toDuration())
                }
            ),
            cursorBrush = SolidColor(Color.Unspecified),
            onValueChange = { newValue ->
                when {
                    newValue.text.length > textValue.length -> {
                        val char = newValue.text.last()

                        if (char.isDigit() && textValue.first() == '0') {
                            time = (textValue.drop(1) + char).toTime()
                            onValueChange?.invoke(time.toDuration())
                        }
                    }

                    newValue.text != textValue -> {
                        time = ('0' + textValue.dropLast(1)).toTime()
                        onValueChange?.invoke(time.toDuration())
                    }
                }
            },
            modifier = modifier
                .width(IntrinsicSize.Min)
                .disableHorizontalScroll()
        )
    }
}

private data class Time(val hours: Int, val minutes: Int, val seconds: Int) {

    override fun toString(): String =
        TIME_PATTERN.format(hours, minutes, seconds)

    fun toDuration(): Duration =
        hours.hours + minutes.minutes + seconds.seconds

    companion object {

        private const val TIME_PATTERN = "%02d:%02d:%02d"

        fun Duration.toTime(): Time =
            toComponents { hours, minutes, seconds, _ ->
                Time(hours.toInt(), minutes, seconds)
            }

        fun String.toTime(): Time =
            filterNot { it == ':' }
                .run {
                    val hours = subSequence(0, 2).toString().toInt()
                    val minutes = subSequence(2, 4).toString().toInt()
                    val seconds = subSequence(4, 6).toString().toInt()

                    Time(hours, minutes, seconds)
                }
    }
}

private fun Modifier.disableHorizontalScroll() =
    nestedScroll(
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                available.copy(y = 0f)

            override suspend fun onPreFling(available: Velocity): Velocity =
                available.copy(y = 0f)
        }
    )

@Preview(showBackground = true)
@Composable
private fun TimePickerPreview() {
    TurnBackTheme {
        TimePicker {}
    }
}
