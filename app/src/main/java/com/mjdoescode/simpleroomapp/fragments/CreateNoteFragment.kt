package com.mjdoescode.simpleroomapp.fragments

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.activities.MainActivity
import com.mjdoescode.simpleroomapp.dao.AppDao
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.databinding.FragmentCreateNoteBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import com.mjdoescode.simpleroomapp.utils.EditTextLimitHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment: Fragment() {

    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDao: AppDao
    private var noteDate = ""

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

        setNoteMaxLines()
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

    private fun createPost(){
        val content = binding.noteContent.text.toString()

        lifecycleScope.launch {
            appDao.insertNote(NotesEntity(
                0,
                content,
                noteDate
            ))
        }

    }
}