package com.mjdoescode.simpleroomapp

import android.app.Application
import com.mjdoescode.simpleroomapp.database.AppDatabase

class MyRoomApp: Application() {

    val db by lazy {
        AppDatabase.getInstance(this)
    }
}