package com.mjdoescode.simpleroomapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.activities.MainActivity
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_CONTENT
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_TITLE
import com.mjdoescode.simpleroomapp.utils.Constants.CHANNEL_ID

class ReminderNotificationService(
    private val context: Context,
    private val noteContent: String
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("noteContent", noteContent)
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_brush_24)
            .setContentTitle("Notes app")
            .setContentText(noteContent)
            .setAutoCancel(true)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }

    companion object {
        const val CHANNEL_NAME = "Reminders"
        const val CHANNEL_DESCRIPTION = "Notifications of reminders you've set up."
    }

}

