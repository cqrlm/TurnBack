package com.example.turnback.ui.stopwatch

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.turnback.ui.theme.TurnBackTheme
import com.example.turnback.ui.theme.Typography
import com.example.turnback.utils.formatTime
import kotlin.time.Duration

@Composable
fun StopwatchScreen(viewModel: StopwatchViewModel = hiltViewModel()) {
    val time = viewModel.timeFlow.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        viewModel.saveTime(time.value)
    }

    StopwatchContent(time.value) {
        viewModel.resetTime()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StopwatchContent(time: Duration, resetTime: () -> Unit) {
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = time.formatTime(),
                    style = Typography.displayLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    var selectedIndex by remember { mutableIntStateOf(0) }

                    val buttonsData = remember {
                        StopwatchButtonData.getAll(
                            startAction = {},
                            pauseAction = {},
                            stopAction = { resetTime() }
                        )
                    }

                    buttonsData.forEachIndexed { index, buttonData ->
                        with(buttonData) {
                            val selected by remember(selectedIndex) {
                                mutableStateOf(index == selectedIndex)
                            }

                            SegmentedButton(
                                selected = selected,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = buttonsData.size
                                ),
                                icon = {},
                                onClick = {
                                    if (!selected) {
                                        selectedIndex = index
                                        onClick()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(iconId),
                                    contentDescription = stringResource(descriptionId)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StopwatchPreview() {
    TurnBackTheme {
        StopwatchContent(Duration.ZERO) {}
    }
}
