package com.mjdoescode.simpleroomapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationService = ReminderNotificationService(context)
        notificationService.showNotification()
    }
}