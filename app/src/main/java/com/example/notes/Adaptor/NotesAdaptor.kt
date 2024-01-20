package com.example.notes.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.R

class NotesAdaptor(private val context: Context, val listener: NoteClickListener):

    RecyclerView.Adapter<NotesAdaptor.NoteViewHolder>() {

    private val notesList = ArrayList<EntityDataBase>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdaptor.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_for_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: NotesAdaptor.NoteViewHolder, position: Int) {
        val item = notesList[position]
        holder.title.text = item.title
        holder.title.isSelected = true
        holder.note.text = item.note
        holder.date.text = item.date
        holder.date.isSelected = true
        holder.note_layout.setOnClickListener {
            listener.onNoteClicked(notesList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun updateList(newList: List<EntityDataBase>) {
        notesList.clear()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val note_layout = itemView.findViewById<CardView>(R.id.card_list)
        val title = itemView.findViewById<TextView>(R.id.list_title)
        val note = itemView.findViewById<TextView>(R.id.list_note)
        val date = itemView.findViewById<TextView>(R.id.list_date)
    }


    interface NoteClickListener {
        fun onNoteClicked(note: EntityDataBase)
    }
}
