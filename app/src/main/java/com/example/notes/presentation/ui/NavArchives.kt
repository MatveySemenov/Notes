package com.example.notes.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.presentation.adapters.NotesAdaptor
import com.example.notes.presentation.notesViewModel.NotesViewModel
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.presentation.addNotes.AddNotes
import com.example.notes.databinding.NavArchivesBinding
import com.example.notes.domain.models.NotesDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NavArchives : Fragment(), NotesAdaptor.NoteClickListener {

    private lateinit var binding: NavArchivesBinding
    lateinit var viewModel: NotesViewModel
    lateinit var adapter: NotesAdaptor

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NavArchivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        adapter.setGuestUser(currentUser == null)

        if (currentUser != null) {
            readNotesFromFirebase()
        } else {
            viewModel.getAllArchivedNotes.observe(viewLifecycleOwner) { list ->
                list?.let {
                    val archivedNotes = it.filter { note -> note.isArchived}
                    adapter.updateArchivedNotesList(archivedNotes)
                }
            }
        }
    }


    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Инициализация адаптера
        adapter = NotesAdaptor(requireContext(), this)
        binding.recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)

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

    override fun onNoteClicked(note: NotesDomain) {
        val intent = Intent(requireContext(), AddNotes::class.java)
        intent.putExtra("current_note", note)
        UpdateOrDeleteNote.launch(intent)
    }

    override fun onNoteClickedFirebase(note: NoteFirebase) {
        val intent = Intent(requireContext(), AddNotes::class.java)
        intent.putExtra("firebase_note", note)
        UpdateOrDeleteNote.launch(intent)
    }

    //выводит архивированные заметки
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
                    val activityNotesFirebase = notesList.filter { noteFirebase -> noteFirebase.isArchived && !noteFirebase.isDelete }
                    adapter.updateArchivedNotesListFirebase(activityNotesFirebase)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Ошибка чтения заметок", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}