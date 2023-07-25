package com.mjdoescode.simpleroomapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import androidx.core.app.NotificationCompat
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.activities.MainActivity
import com.mjdoescode.simpleroomapp.fragments.EditNoteFragment
import com.mjdoescode.simpleroomapp.fragments.MainFragment
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_CONTENT
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_TITLE

class ReminderNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val defaultVibratePattern = longArrayOf(0, 250, 250, 250)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_brush_24)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_CONTENT)
            .setAutoCancel(true)
            .setContentIntent(activityPendingIntent)
            .setVibrate(defaultVibratePattern)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }

    companion object {
        const val CHANNEL_ID = "my_channel_id"
        const val CHANNEL_NAME = "My Channel"
        const val CHANNEL_DESCRIPTION = "My Channel Description"
    }

}

