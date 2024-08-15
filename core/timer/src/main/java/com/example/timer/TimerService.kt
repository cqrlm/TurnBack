package com.example.timer

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.timer.notification.NotificationAction
import com.example.timer.notification.NotificationConstants
import com.example.timer.notification.NotificationManager
import com.example.timer.state.TimerServiceActions
import com.example.timer.state.TimerServiceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration

@AndroidEntryPoint
class TimerService : Service(), CoroutineScope by MainScope() {

    @Inject
    internal lateinit var notificationManager: NotificationManager

    @Inject
    internal lateinit var timerManager: TimerManager

    val timerServiceState by lazy {
        combine(
            timerManager.timeFlow,
            timerManager.timerState
        ) { time, timerState ->
            TimerServiceState(
                timerDuration = time,
                timerState = timerState
            )
        }.stateIn(this, SharingStarted.Lazily, TimerServiceState())
    }

    val timerServiceActions = TimerServiceActions(
        start = ::start,
        pause = ::pause,
        resume = ::resume,
        stop = ::stop
    )

    private val binder by lazy { TimerBinder() }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                NotificationAction.PAUSE.name -> pause()
                NotificationAction.RESUME.name -> resume()
                NotificationAction.STOP.name -> stop()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    private fun start(
        duration: Duration,
        context: Context,
        activityClassName: Class<out Activity>
    ) {
        startService(context)
        startForeground(activityClassName)
        timerManager.start(duration)
    }

    private fun resume() {
        timerManager.resume()
    }

    private fun pause() {
        timerManager.pause()
    }

    private fun stop() {
        timerManager.stop()
        stopService()
    }

    private fun startService(context: Context) {
        ContextCompat.startForegroundService(context, Intent(context, this::class.java))
    }

    private fun startForeground(activityClassName: Class<out Activity>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NotificationConstants.NOTIFICATION_ID,
                notificationManager.getNotification(activityClassName),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(
                NotificationConstants.NOTIFICATION_ID,
                notificationManager.getNotification(activityClassName)
            )
        }
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    inner class TimerBinder : Binder() {

        fun getService(): TimerService = this@TimerService
    }
}
