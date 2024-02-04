package com.example.notes.UI.trash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.Adaptor.NotesAdaptor
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.DataBase.NotesViewModel
import com.example.notes.ListUser.NoteFirebase
import com.example.notes.add_notes
import com.example.notes.databinding.NavTrashBinding
import com.google.firebase.auth.FirebaseAuth

class NavTrash: Fragment(), NotesAdaptor.NoteClickListener {

    private lateinit var binding: NavTrashBinding
    lateinit var viewModel: NotesViewModel
    lateinit var adapter: NotesAdaptor

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NavTrashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        adapter.setGuestUser(currentUser == null)

        if (currentUser != null){

        } else {
            viewModel.getAllDeleteNotes.observe(viewLifecycleOwner){ list ->
                list?.let {
                    val deleteNotes = it.filter { note -> note.isDelete }
                    adapter.updateDeleteNotesList(deleteNotes)
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