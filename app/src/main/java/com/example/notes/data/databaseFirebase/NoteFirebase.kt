package com.example.notes.data.databaseFirebase

import java.io.Serializable

class NoteFirebase(
    var id: String? = "",
    var title: String = "",
    var text: String = "",
    var date: String = "",
    var isArchived: Boolean = false,
    var isDelete: Boolean = false
) : Serializable