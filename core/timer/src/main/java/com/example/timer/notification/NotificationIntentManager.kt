package com.example.timer.notification

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.timer.TimerService
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
internal class NotificationIntentManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun clickIntent(activityClassName: Class<out Activity>): PendingIntent {
        val intent = Intent(context, activityClassName).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            NotificationConstants.CLICK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val pauseIntent: PendingIntent = run {
        val intent = Intent(context, TimerService::class.java).apply {
            action = NotificationAction.PAUSE.name
        }

        PendingIntent.getService(
            context,
            NotificationConstants.PAUSE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val resumeIntent: PendingIntent = run {
        val intent = Intent(context, TimerService::class.java).apply {
            action = NotificationAction.RESUME.name
        }

        PendingIntent.getService(
            context,
            NotificationConstants.RESUME_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val stopIntent: PendingIntent = run {
        val intent = Intent(context, TimerService::class.java).apply {
            action = NotificationAction.STOP.name
        }

        PendingIntent.getService(
            context,
            NotificationConstants.STOP_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
