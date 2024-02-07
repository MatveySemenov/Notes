package com.example.notes.presentation.notesViewModel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.presentation.adapters.NotesAdaptor
import com.example.notes.data.database.NotesDataBase
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.presentation.addNotes.AddNotes
import com.example.notes.databinding.NotesBookBinding
import com.example.notes.domain.models.NotesDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesBook: Fragment(), NotesAdaptor.NoteClickListener {

    private lateinit var binding: NotesBookBinding
    private lateinit var database: NotesDataBase
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesAdaptor

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = NotesBookBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        adapter.setGuestUser(currentUser == null)

        if (currentUser != null) {
            readNotesFromFirebase()
        } else{
            viewModel.allNotes.observe(viewLifecycleOwner){list ->
                list?.let {
                    val activeNotes = it.filter { note -> !note.isArchived && !note.isDelete}
                    adapter.updateList(activeNotes)
                }
            }
        }
        database = NotesDataBase.getDataBase(requireContext())
    }


    private fun initUI(){
        //инициализируем адаптер
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = NotesAdaptor(requireContext(), this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (currentUser != null){
                        val note = result.data?.getSerializableExtra("noteFirebase") as? NoteFirebase
                        if (note != null){
                            viewModel.insertFirebase(note)
                        }
                    } else {
                        val note = result.data?.getSerializableExtra("note") as? NotesDomain
                        if (note != null) {
                            viewModel.insertNote(note)
                        }
                    }
                }
            }

        binding.addNote.setOnClickListener {
            val intent = Intent(requireContext(), AddNotes::class.java)
            getContent.launch(intent)
        }
    }

    private val UpdateOrDeleteNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (currentUser != null){
                    //Пользователь авторизован, выполняются операции Firebase
                    val noteFirebase = result.data?.getSerializableExtra("noteFirebase") as NoteFirebase
                    val isDeleteFirebase = result.data?.getBooleanExtra("delete_noteFirebase",false) as Boolean

                    if(!isDeleteFirebase){
                        viewModel.updateFirebaseNote(noteFirebase)
                    } else {
                        viewModel.deleteFirebaseNote(noteFirebase)
                    }

                } else {
                    val note = result.data?.getSerializableExtra("note") as NotesDomain
                    val isDelete = result.data?.getBooleanExtra("delete_note", false) as Boolean

                    if (!isDelete) {
                        viewModel.updateNote(note)
                    } else if (isDelete) {
                        viewModel.deleteNote(note)
                    }
                }
            }
        }

    // для локальной
    override fun onNoteClicked(note: NotesDomain) {
        val intent = Intent(requireContext(), AddNotes::class.java)
        intent.putExtra("current_note", note)
        UpdateOrDeleteNote.launch(intent)
    }

    //для серверной
    override fun onNoteClickedFirebase(note: NoteFirebase) {
        val intent = Intent(requireContext(), AddNotes::class.java)
        intent.putExtra("firebase_note", note)
        UpdateOrDeleteNote.launch(intent)
    }

    private fun readNotesFromFirebase() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid)

            notesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val notesList = mutableListOf<NoteFirebase>()

                    for (noteSnapshot in dataSnapshot.children) {
                        val note = noteSnapshot.getValue(NoteFirebase::class.java)
                        note?.let {
                            notesList.add(it)
                        }
                    }
                    val activityNotesFirebase = notesList.filter { noteFirebase -> !noteFirebase.isArchived && !noteFirebase.isDelete }
                    adapter.updateFirebaseList(activityNotesFirebase)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Ошибка чтения заметок", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}