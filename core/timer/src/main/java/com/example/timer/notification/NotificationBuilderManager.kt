package com.example.timer.notification

import android.app.Activity
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.timer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
internal class NotificationBuilderManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationIntentManager: NotificationIntentManager
) {

    private var activityClassName: Class<out Activity> = Activity::class.java

    private val defaultNotificationBuilder: NotificationCompat.Builder
        get() = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentText(context.getString(R.string.timer))
            .setShowWhen(false)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(notificationIntentManager.clickIntent(activityClassName))

    val tickNotificationBuilder: NotificationCompat.Builder
        get() = defaultNotificationBuilder
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                NotificationCompat.Action(
                    null,
                    context.getString(R.string.pause),
                    notificationIntentManager.pauseIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    null,
                    context.getString(R.string.stop),
                    notificationIntentManager.stopIntent
                )
            )

    val pauseNotificationBuilder: NotificationCompat.Builder
        get() = defaultNotificationBuilder
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                NotificationCompat.Action(
                    null,
                    context.getString(R.string.start),
                    notificationIntentManager.resumeIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    null,
                    context.getString(R.string.stop),
                    notificationIntentManager.stopIntent
                )
            )

    val finishNotificationBuilder: NotificationCompat.Builder
        get() = defaultNotificationBuilder
            .setContentTitle(context.getString(R.string.time_is_up))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSilent(false)
            .setOngoing(false)
            .setAutoCancel(true)

    fun build(activityClassName: Class<out Activity>): Notification {
        this.activityClassName = activityClassName
        return defaultNotificationBuilder.build()
    }
}
