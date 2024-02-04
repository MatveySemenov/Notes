package com.example.notes.UI.archives

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.Adaptor.NotesAdaptor
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.DataBase.NotesViewModel
import com.example.notes.ListUser.NoteFirebase
import com.example.notes.add_notes
import com.example.notes.databinding.NavArchivesBinding
import com.google.firebase.auth.FirebaseAuth

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
                val note = result.data?.getSerializableExtra("note") as EntityDataBase
                val isDelete = result.data?.getBooleanExtra("delete_note", false) as Boolean

                if (!isDelete) {
                    viewModel.updateNote(note)
                } else if (isDelete) {
                    viewModel.deleteNote(note)
                }
            }
        }

    override fun onNoteClicked(note: EntityDataBase) {
        val intent = Intent(requireContext(), add_notes::class.java)
        intent.putExtra("current_note", note)
        UpdateOrDeleteNote.launch(intent)
    }

    override fun onNoteClickedFirebase(note: NoteFirebase) {
        TODO("Not yet implemented")
    }

}