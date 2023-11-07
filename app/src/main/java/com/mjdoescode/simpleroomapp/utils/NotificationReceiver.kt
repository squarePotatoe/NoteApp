package com.mjdoescode.simpleroomapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val content = intent.getStringExtra("noteContent")
        val notificationService = ReminderNotificationService(context, content ?: "")

        println("Did this get called $content")

        notificationService.showNotification()
    }

}