package com.mjdoescode.simpleroomapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mjdoescode.simpleroomapp.activities.MainActivity
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_CONTENT
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_TITLE
import com.mjdoescode.simpleroomapp.utils.Constants.CHANNEL_DESCRIPTION
import com.mjdoescode.simpleroomapp.utils.Constants.CHANNEL_ID
import com.mjdoescode.simpleroomapp.utils.Constants.CHANNEL_NAME

class MyRoomApp: Application() {

    val db by lazy {
        AppDatabase.getInstance(this)
    }

    fun showNotification(context: Context){
        val channelId = CHANNEL_ID
        val channelName = CHANNEL_NAME
        val channelDescription = CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationChannel = NotificationChannel(channelId, channelName, importance). apply {
            description = channelDescription
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationTitle = NOTIFICATION_TITLE
        val notificationContent = NOTIFICATION_CONTENT
        val notificationIcon = R.drawable.baseline_brush_24

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(notificationIcon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()

        val notificationId = 1
        notificationManager.notify(notificationId, notification)
    }

}