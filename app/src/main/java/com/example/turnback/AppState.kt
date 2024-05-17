package com.example.turnback

import com.example.turnback.model.TimerPreset
import com.example.turnback.navigaiton.Screen

sealed class AppState(open val screen: Screen = Screen.BottomBarItem.Timer) {

    class Idle(override val screen: Screen = Screen.BottomBarItem.Timer) : AppState(screen)

    class Deletion(val selectedTimerPresetsCount: Int) : AppState()

    class Editing(val editingTimerPreset: TimerPreset = TimerPreset.Undefined) : AppState()
}
