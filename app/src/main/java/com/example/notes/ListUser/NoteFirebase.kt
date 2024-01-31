package com.example.notes.ListUser

import java.io.Serializable

class NoteFirebase(
    var id: String = "",
    var title: String = "",
    var text: String = "",
    var date: String = ""
) : Serializable