package com.mjdoescode.simpleroomapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.commit
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

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.note_color_yellow -> {
            noteColor = findViewById(R.id.recycler_card)
            noteColor?.setCardBackgroundColor(getColor(R.color.note_color_yellow))
            true
        }
        R.id.note_color_pink -> {
            noteColor = findViewById(R.id.recycler_card)
            noteColor?.setCardBackgroundColor(getColor(R.color.note_color_pink))
            true
        }
        R.id.note_color_turq -> {
            noteColor = findViewById(R.id.recycler_card)
            noteColor?.setCardBackgroundColor(getColor(R.color.note_color_turquoise))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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

}