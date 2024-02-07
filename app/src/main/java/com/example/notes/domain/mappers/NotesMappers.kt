package com.example.notes.domain.mappers

import com.example.notes.data.database.EntityDataBase
import com.example.notes.domain.models.NotesDomain

fun EntityDataBase.toDomainModel(): NotesDomain {
    return NotesDomain(id, title, note, date, isArchived, isDelete)
}

fun NotesDomain.toEntity(): EntityDataBase {
    return EntityDataBase(id, title, note, date, isArchived, isDelete)
}