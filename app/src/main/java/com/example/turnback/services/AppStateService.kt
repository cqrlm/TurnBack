package com.example.turnback.services

import com.example.turnback.AppState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class AppStateService @Inject constructor() {

    private val _appStateFlow = MutableStateFlow<AppState>(AppState.Idle())
    val appStateFlow = _appStateFlow.asStateFlow()

    val isDeletionState: Boolean
        get() = _appStateFlow.value is AppState.Deletion

    fun setAppState(appState: AppState) {
        _appStateFlow.value = appState
    }
}
