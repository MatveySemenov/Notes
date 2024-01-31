package com.example.notes.Adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.ListUser.NoteFirebase
import com.example.notes.R

class NotesAdaptor(private val context: Context, val listener: NoteClickListener):

    RecyclerView.Adapter<NotesAdaptor.NoteViewHolder>() {

    private val notesList = ArrayList<EntityDataBase>()
    private var notesListFirebase: List<NoteFirebase> = emptyList()
    private var isGuestUser: Boolean = false

    //Метод для установки флага гостевого пользователя
    fun setGuestUser(isGuest:Boolean){
        isGuestUser = isGuest
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdaptor.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_for_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: NotesAdaptor.NoteViewHolder, position: Int) {
        when{
            position < notesList.size && isGuestUser -> {
                // Если заметка из локальной базы и пользователь гость
                val item = notesList.getOrNull(position)
                if (item != null){
                    holder.title.text = item.title
                    holder.title.isSelected = true
                    holder.note.text = item.note
                    holder.date.text = item.date
                    holder.date.isSelected = true
                    holder.note_layout.setOnClickListener {
                        listener.onNoteClicked(notesList[holder.adapterPosition])
                    }
                }
            }
            position < notesListFirebase.size && !isGuestUser -> {
                //Если это заметка из Firebase
                val item = notesListFirebase.getOrNull(position)
                if (item != null){
                    holder.title.text = item.title
                    holder.title.isSelected = true
                    holder.note.text = item.text
                    holder.date.text = item.date
                    holder.date.isSelected = true
                    holder.note_layout.setOnClickListener {
                        listener.onNoteClickedFirebase(notesListFirebase[holder.adapterPosition])
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return maxOf(notesList.size,notesListFirebase.size)
    }


    fun updateList(newList: List<EntityDataBase>) {
        notesList.clear()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateFirebaseList(newList: List<NoteFirebase>){
        notesListFirebase = newList
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
        fun onNoteClickedFirebase(note: NoteFirebase)
    }
}
