package com.mjdoescode.simpleroomapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.utils.Constants.CHANNEL_ID
import com.mjdoescode.simpleroomapp.utils.ReminderNotificationService.Companion.CHANNEL_DESCRIPTION
import com.mjdoescode.simpleroomapp.utils.ReminderNotificationService.Companion.CHANNEL_NAME

class MyRoomApp: Application() {

    val db by lazy {
        AppDatabase.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



}