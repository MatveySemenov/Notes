package com.example.notes.domain.models

data class NotesDomain(
    val id: Int?,
    val title: String?,
    val note: String?,
    val date: String,
    var isArchived: Boolean,
    var isDelete: Boolean
): java.io.Serializable
