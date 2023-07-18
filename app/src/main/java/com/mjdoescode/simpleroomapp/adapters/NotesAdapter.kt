package com.mjdoescode.simpleroomapp.adapters

import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mjdoescode.simpleroomapp.databinding.RecyclerNotesBinding
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import kotlin.collections.ArrayList

class NotesAdapter(
    private val notes: ArrayList<NotesEntity>
) : RecyclerView.Adapter<NotesAdapter.MainViewHolder>(){

    private var onClickListener: OnClickListener? = null
    private var onLongPressListener: OnLongPressListener? = null

    inner class MainViewHolder(private val binding: RecyclerNotesBinding) :
        ViewHolder(binding.root) {

        val noteContent = binding.noteContent
        val noteDate = binding.noteDateStamp
        val notePin = binding.notePin
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            RecyclerNotesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val note = notes[notes.size - position - 1]

        holder.noteContent.text = note.noteContent
        holder.noteDate.text = note.noteTimeStamp

        holder.notePin.setOnClickListener {
            onClickListener?.onDeleteClicked(position, note)
        }

        holder.itemView.setOnLongClickListener {
            onLongPressListener?.onLongPress(position, note) ?: false
        }

    }

    fun setOnClickListener(listener: OnClickListener){
        onClickListener = listener
    }

    fun setOnLongPressListener(listener: OnLongPressListener){
        onLongPressListener = listener
    }

    fun getPositionOfNote(note: NotesEntity): Int{
        return notes.size - notes.indexOf(note) - 1
    }

    fun removeNoteAtPosition(position: Int) {
        if (position in 0 until notes.size) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    interface OnClickListener {
        fun onDeleteClicked(position: Int, note: NotesEntity)
    }

    interface OnLongPressListener {
        fun onLongPress(position: Int, note: NotesEntity): Boolean
    }

}