package com.example.timer.notification

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.common.formatElapsedTime
import com.example.timer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject
import kotlin.time.Duration

@ServiceScoped
internal class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationBuilderManager: NotificationBuilderManager
) {

    init {
        createNotificationChannel()
    }

    fun getNotification(activityClassName: Class<out Activity>): Notification =
        notificationBuilderManager.build(activityClassName)

    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    fun notifyTimerTick(timeLeft: Duration) {
        notify(
            notification = notificationBuilderManager
                .tickNotificationBuilder
                .setContentTitle(timeLeft.formatElapsedTime())
                .build()
        )
    }

    fun notifyTimerPause(timeLeft: Duration) {
        notify(
            notification = notificationBuilderManager
                .pauseNotificationBuilder
                .setContentTitle(timeLeft.formatElapsedTime())
                .build()
        )
    }

    fun notifyTimerFinish() {
        notify(
            notification = notificationBuilderManager
                .finishNotificationBuilder
                .build()
                .apply { flags = Notification.FLAG_INSISTENT }
        )
    }

    fun cancelNotification() {
        notificationManagerCompat.cancel(NotificationConstants.NOTIFICATION_ID)
    }

    private fun notify(notification: Notification) {
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
            notification
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.timer_notifications)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
                .apply {
                    setShowBadge(false)
                    setSound(
                        Uri.parse(
                            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/" +
                                "${R.raw.sound_time_is_up}"
                        ),
                        Notification.AUDIO_ATTRIBUTES_DEFAULT
                    )
                }

            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?.createNotificationChannel(channel)
        }
    }
}
