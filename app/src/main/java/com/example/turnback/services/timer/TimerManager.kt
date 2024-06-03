package com.example.turnback.services.timer

import com.example.turnback.services.timer.notification.NotificationManager
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@ServiceScoped
class TimerManager @Inject constructor(private val notificationManager: NotificationManager) {

    private val _timeFlow = MutableStateFlow(Duration.ZERO)
    val timeFlow = _timeFlow.asStateFlow()

    private var _timerStateFlow = MutableStateFlow(TimerState.STOP)
    val timerState = _timerStateFlow.asStateFlow()

    private var timer: Timer? = null

    fun start(duration: Duration) {
        var time = duration

        _timerStateFlow.value = TimerState.START

        timer = fixedRateTimer(period = TIME_INTERVAL) {
            _timeFlow.value = time
            notificationManager.notifyTimerTick(time)

            if (time == Duration.ZERO) {
                stop()
                notificationManager.notifyTimerFinish()
            } else time -= TIME_INTERVAL_DURATION
        }
    }

    fun pause() {
        timer?.cancel()
        notificationManager.notifyTimerPause(_timeFlow.value)
        _timerStateFlow.value = TimerState.PAUSE
    }

    fun resume() {
        start(_timeFlow.value)
    }

    fun stop() {
        timer?.cancel()
        notificationManager.cancelNotification()
        _timerStateFlow.value = TimerState.STOP
    }

    companion object {
        private const val TIME_INTERVAL = 1000L
        private val TIME_INTERVAL_DURATION = TIME_INTERVAL.milliseconds
    }
}
