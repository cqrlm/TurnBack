package com.example.turnback.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class ScreenViewModel<S : ScreenState, A : ScreenActions> : ViewModel() {

    abstract val screenState: StateFlow<S>

    abstract val screenActions: A

    @Composable
    fun collectState(): State<S> = screenState.collectAsState()
}
