package com.mjdoescode.simpleroomapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mjdoescode.simpleroomapp.dao.AppDao
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.databinding.FragmentCreateNoteBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import com.mjdoescode.simpleroomapp.models.Reminder
import com.mjdoescode.simpleroomapp.utils.EditTextLimitHelper
import com.mjdoescode.simpleroomapp.utils.NotificationReceiver
import com.mjdoescode.simpleroomapp.utils.ReminderManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

const val TAG = "CreateNoteFragment"
class CreateNoteFragment : Fragment() {

    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDao: AppDao

    private lateinit var calendar: Calendar

    private lateinit var reminder: Reminder

    private var content = ""
    private var noteDate = ""
    private var noteReminderTime: LocalDateTime? = null

    private var selectedDate: LocalDateTime? = null
    private var selectedTime: LocalDateTime? = null

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
        calendar = Calendar.getInstance()

        reminderManager = ReminderManager(requireContext())

        setupNoteDate()

        setNoteMaxLines()

        setupClickListeners()
    }

    private fun setupClickListeners() {

        binding.buttonBack.setOnClickListener {
            exitFragmentAfterUpdate()
        }

        binding.checkboxReminder.setOnCheckedChangeListener { _, isChecked ->
            showDatePicker()
            binding.reminderTime.isVisible = isChecked
        }

        binding.buttonSave.setOnClickListener {
            createPost()
            exitFragmentAfterUpdate()
        }

    }



    private fun showDatePicker() {
        val currentDate = LocalDateTime.now()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
                showTimePicker()
            },
            currentDate.year,
            currentDate.monthValue,
            currentDate.dayOfMonth
        )

        datePickerDialog.datePicker.minDate =
            currentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        datePickerDialog.show()
    }
    private fun showTimePicker() {
        val currentTime = LocalDateTime.now()

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedTime = LocalDateTime.of(
                    selectedDate!!.year,
                    selectedDate!!.month,
                    selectedDate!!.dayOfMonth,
                    hourOfDay,
                    minute
                )
                binding.reminderTime.text =
                    selectedTime!!.format(DateTimeFormatter.ofPattern("hh:mm"))

                noteReminderTime = selectedTime

                Log.e(TAG, "Reminder time: $noteReminderTime")
            },
            currentTime.hour,
            currentTime.minute,
            false
        )

        timePickerDialog.show()
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

    private fun exitFragmentAfterUpdate() {
        parentFragmentManager.popBackStack()
    }

    private fun createPost() {
        content = binding.noteContent.text.toString()

        reminder = Reminder(content, noteReminderTime ?: LocalDateTime.now())

        reminderManager.schedule(reminder)


        Log.e("TAG", "createPost: $content ${noteReminderTime.toString()} $noteDate", )

        lifecycleScope.launch {
            appDao.insertNote(
                NotesEntity(
                    content,
                    selectedDate.toString(),
                    noteDate
                )
            )
        }

    }
}