package com.mjdoescode.simpleroomapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjdoescode.simpleroomapp.MyRoomApp
import com.mjdoescode.simpleroomapp.R
import com.mjdoescode.simpleroomapp.adapters.NotesAdapter
import com.mjdoescode.simpleroomapp.dao.AppDao
import com.mjdoescode.simpleroomapp.database.AppDatabase
import com.mjdoescode.simpleroomapp.databinding.FragmentMainBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Thread.State

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDao: AppDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        appDao = appDatabase.appDao()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.fabNote.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_content, CreateNoteFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getAllPosts(){
        lifecycleScope.launch {
            appDao.getAllNotes().collect(){
                val listOfPosts = ArrayList(it)
                setupNotesRecycler(listOfPosts)
            }
        }
    }
    private fun openEditNoteFragment(noteId: Int) {
        val editNoteFragment = EditNoteFragment()
        val bundle = Bundle()
        bundle.putInt("noteId", noteId)
        editNoteFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_content, editNoteFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupNotesRecycler(listOfPosts: ArrayList<NotesEntity>) {

            notesAdapter = NotesAdapter(listOfPosts)
            binding.notesRecycler.adapter = notesAdapter

            val layoutManager = LinearLayoutManager(requireContext())
            binding.notesRecycler.layoutManager = layoutManager

            notesAdapter.setOnClickListener(object : NotesAdapter.OnClickListener{
                override fun onDeleteClicked(position: Int, note: NotesEntity) {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setMessage("Delete note?")
                        .setPositiveButton("Yes") {_, _ ->
                            lifecycleScope.launch {
                                appDao.deleteNoteAtId(note.noteId)
                                val position = notesAdapter.getPositionOfNote(note)
                                notesAdapter.removeNoteAtPosition(position)
                            }
                        }
                        .setNegativeButton("No", null)
                        .create()
                    alertDialog.show()
                }

            })

            notesAdapter.setOnLongPressListener(object : NotesAdapter.OnLongPressListener{
                override fun onLongPress(position: Int, note: NotesEntity): Boolean {
                    openEditNoteFragment(note.noteId)
                    return true
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()
    }
}