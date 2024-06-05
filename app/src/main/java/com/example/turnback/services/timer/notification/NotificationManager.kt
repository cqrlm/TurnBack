package com.example.turnback.services.timer.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.turnback.R
import com.example.turnback.utils.formatElapsedTime
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject
import kotlin.time.Duration

@ServiceScoped
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationBuilderManager: NotificationBuilderManager
) {

    init {
        createNotificationChannel()
    }

    val notification: Notification
        get() = notificationBuilderManager.defaultNotificationBuilder.build()

    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    fun notifyTimerTick(timeLeft: Duration) {
        notify(
            builder = notificationBuilderManager
                .tickNotificationBuilder
                .setContentTitle(timeLeft.formatElapsedTime())
        )
    }

    fun notifyTimerPause(timeLeft: Duration) {
        notify(
            builder = notificationBuilderManager
                .pauseNotificationBuilder
                .setContentTitle(timeLeft.formatElapsedTime())
        )
    }

    fun notifyTimerFinish() {
        notify(builder = notificationBuilderManager.finishNotificationBuilder)
    }

    fun cancelNotification() {
        notificationManagerCompat.cancel(NotificationConstants.NOTIFICATION_ID)
    }

    private fun notify(builder: NotificationCompat.Builder) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationManagerCompat.notify(
            NotificationConstants.NOTIFICATION_ID,
            builder.build()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.timer_notifications)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
                .apply { setShowBadge(false) }

            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?.createNotificationChannel(channel)
        }
    }
}
