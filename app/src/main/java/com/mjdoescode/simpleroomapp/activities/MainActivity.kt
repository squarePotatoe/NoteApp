package com.mjdoescode.simpleroomapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var noteColor: CardView? = null
    private var canGoBack = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        binding.notify.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                (application as MyRoomApp).showNotification(this)
            }, 10000)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onBackPressed() {
        if (canGoBack){
            onBackPressedDispatcher.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to close the app.", Toast.LENGTH_SHORT).show()
        }
    }

    fun canGoBack(canGoBack: Boolean){
        this.canGoBack = canGoBack
    }

    companion object {
        const val CHANNEL_ID = "my_channel_id"
    }
}