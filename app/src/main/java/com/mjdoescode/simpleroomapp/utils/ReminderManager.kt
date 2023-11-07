package com.mjdoescode.simpleroomapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mjdoescode.simpleroomapp.interfaces.AlarmScheduler
import com.mjdoescode.simpleroomapp.models.Reminder
import java.time.ZoneId

class ReminderManager(private val context: Context): AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setReminder(timeInMillis: Long) {
        val alarmIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    override fun schedule(item: Reminder) {
        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("noteContent", item.content)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            pendingIntent
        )
//
//        val interval = AlarmManager.INTERVAL_DAY
//        val triggerAtMillis = item.dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            triggerAtMillis,
//            interval,
//            pendingIntent
//        )
    }

    private fun calculateIntervalMillis(item: Reminder): Long {
        return 24 * 60 * 60 * 1000
    }

    override fun cancel(item: Reminder) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}