package com.mjdoescode.simpleroomapp.activities


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mjdoescode.simpleroomapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var noteColor: CardView? = null
    private var canGoBack = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        setupPermissions()
    }

    private fun setupPermissions() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM,
                android.Manifest.permission.USE_EXACT_ALARM,
                android.Manifest.permission.VIBRATE
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                        return
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(
            "Without permissions, you won't be able to set reminders. You will have to change that in the settings"
        )
            .setPositiveButton("Go to settings") {_,_ ->
                try {
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
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

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST = 1
    }
}