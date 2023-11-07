package com.mjdoescode.simpleroomapp.fragments

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.activities.MainActivity
import com.mjdoescode.simpleroomapp.dao.AppDao
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.databinding.FragmentEditNoteBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import com.mjdoescode.simpleroomapp.utils.EditTextLimitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteFragment : Fragment() {
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDao: AppDao
    private var noteId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        appDao = appDatabase.appDao()

        noteId = arguments?.getInt("noteId") ?: return
        Log.e("TAG", "onViewCreated: $noteId", )

        setupClickListeners()

        setNoteMaxLines()
    }

    private fun setNoteMaxLines() {
        val editText = binding.content
        val maxNewLines = 12

        val inputFilter = arrayOf(
            InputFilter.LengthFilter(Integer.MAX_VALUE),
            EditTextLimitHelper(maxNewLines)
        )

        editText.filters = inputFilter
    }
    private fun setupClickListeners() {
        binding.update.setOnClickListener {
            updatePost()
        }

    }
    private fun getNote() {
        lifecycleScope.launch {
            try {
                appDao.getNoteById(noteId!!).collect { note ->
                    binding.content.setText(note.noteContent)
                        if (note.noteReminderTime != ""){
                            binding.checkboxReminder.isChecked = true
                        } else {
                            binding.reminderTime.isVisible = true
                            binding.reminderTime.text = "No reminder time was set!"
                        }
                    }
            } catch (e: IllegalAccessError) {
                Log.e("NOTEID", "getNote: ${IllegalAccessError()}")
            }
        }
    }

    private fun updatePost() {
        val content = binding.content.text.toString()
        val id = noteId ?: return

        lifecycleScope.launch {

            appDao.getNoteById(id).collect() { note ->
                note.noteContent = content
                appDao.updateNote(note)

                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getNote()
    }
}