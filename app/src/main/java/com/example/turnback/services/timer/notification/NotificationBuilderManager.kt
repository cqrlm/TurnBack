package com.example.turnback.services.timer.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.turnback.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class NotificationBuilderManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationIntentManager: NotificationIntentManager
) {

    val defaultNotificationBuilder: NotificationCompat.Builder
        get() = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentText(context.getString(R.string.timer))
            .setShowWhen(false)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSilent(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(notificationIntentManager.clickIntent)

    val tickNotificationBuilder = defaultNotificationBuilder
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

    val pauseNotificationBuilder = defaultNotificationBuilder
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

    val finishNotificationBuilder = defaultNotificationBuilder
        .setContentTitle(context.getString(R.string.time_is_up))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setSilent(false)
        .setOngoing(false)
        .setAutoCancel(true)
}
