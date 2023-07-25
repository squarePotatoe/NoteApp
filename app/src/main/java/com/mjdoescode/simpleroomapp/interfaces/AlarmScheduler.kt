package com.mjdoescode.simpleroomapp.interfaces

import com.mjdoescode.simpleroomapp.models.Reminder

interface AlarmScheduler {
    fun schedule(item: Reminder)
    fun cancel(item: Reminder)
}