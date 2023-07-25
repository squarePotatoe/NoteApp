package com.mjdoescode.simpleroomapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mjdoescode.simpleroomapp.MyRoomApp
import com.mjdoescode.simpleroomapp.dao.AppDao
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.databinding.FragmentCreateNoteBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import com.mjdoescode.simpleroomapp.utils.Configs.NOTIFICATION_CONTENT
import com.mjdoescode.simpleroomapp.utils.EditTextLimitHelper
import com.mjdoescode.simpleroomapp.utils.ReminderManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : Fragment() {

    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDao: AppDao


    private var content = ""
    private var noteDate = ""
    private var noteReminderTime = ""
    private var noteId = 0

    private lateinit var reminderManager: ReminderManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        appDao = appDatabase.appDao()

        setupClickListeners()
        setupNoteDate()

        reminderManager = ReminderManager(requireContext())

        binding.checkboxReminder.setOnCheckedChangeListener { _, isChecked ->
            binding.reminderTime.isVisible = isChecked
        }

        binding.reminderTime.setOnClickListener {
            setupReminder()
        }

        setNoteMaxLines()
    }

    private fun setupReminder() {

        val timePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time: ")
                .build()

            timePicker.show(parentFragmentManager, "Reminder")

            Log.e("TAG", "setupReminder: $timePicker")

            timePicker.addOnPositiveButtonClickListener {
                if (timePicker.hour > 12) {
                    binding.reminderTime.text =
                        String.format("%02d", timePicker.hour - 12) + " : " +
                                String.format("%02d", timePicker.minute) + "PM"
                } else {
                    String.format("%02d", timePicker.hour) + " : " + String.format(
                        "%02d",
                        timePicker.minute
                    ) + "AM"
                }

                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
                calendar[Calendar.MINUTE] = timePicker.minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                noteReminderTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

                reminderManager.setReminder(calendar.timeInMillis)
                NOTIFICATION_CONTENT = content
            }
    }

    private fun setNoteMaxLines() {
        val editText = binding.noteContent
        val maxNewLines = 12

        val inputFilter = arrayOf<InputFilter>(
            InputFilter.LengthFilter(Integer.MAX_VALUE),
            EditTextLimitHelper(maxNewLines)
        )

        editText.filters = inputFilter
    }

    private fun setupNoteDate() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm dd/MMMM", Locale.getDefault())
        noteDate = sdf.format(calendar.time)
    }

    private fun setupClickListeners() {
        binding.buttonSave.setOnClickListener {
            createPost()
            exitFragmentAfterUpdate()
        }
        binding.buttonBack.setOnClickListener {
            exitFragmentAfterUpdate()
        }
    }

    private fun exitFragmentAfterUpdate() {
        parentFragmentManager.popBackStack()
    }

    private fun createPost() {
        content = binding.noteContent.text.toString()

        lifecycleScope.launch {
            appDao.insertNote(
                NotesEntity(
                    noteId,
                    content,
                    noteReminderTime,
                    noteDate
                )
            )

        }

    }
}