package com.example.turnback.services.timer

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.turnback.services.timer.notification.NotificationAction
import com.example.turnback.services.timer.notification.NotificationConstants
import com.example.turnback.services.timer.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration

@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var timerManager: TimerManager

    val timeFlow by lazy { timerManager.timeFlow }

    val timerState by lazy { timerManager.timerState }

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

    fun start(duration: Duration, context: Context, activityClassName: Class<out Activity>) {
        startService(context)
        start(duration, activityClassName)
    }

    private fun start(duration: Duration, activityClassName: Class<out Activity>) {
        startForeground(activityClassName)
        timerManager.start(duration)
    }

    fun resume() {
        timerManager.resume()
    }

    fun pause() {
        timerManager.pause()
    }

    fun stop() {
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
