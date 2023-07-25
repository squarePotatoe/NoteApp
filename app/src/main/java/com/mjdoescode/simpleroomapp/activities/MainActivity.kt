package com.mjdoescode.simpleroomapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.mjdoescode.simpleroomapp.MyRoomApp
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.databinding.ActivityMainBinding
import com.mjdoescode.simpleroomapp.fragments.CreateNoteFragment
import com.mjdoescode.simpleroomapp.fragments.MainFragment
import com.mjdoescode.simpleroomapp.utils.NotificationReceiver
import com.mjdoescode.simpleroomapp.utils.ReminderManager
import com.mjdoescode.simpleroomapp.utils.ReminderNotificationService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var noteColor: CardView? = null
    private var canGoBack = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onBackPressed() {
        if (canGoBack) {
            onBackPressedDispatcher.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to close the app.", Toast.LENGTH_SHORT).show()
        }
    }

    fun canGoBack(canGoBack: Boolean) {
        this.canGoBack = canGoBack
    }

}