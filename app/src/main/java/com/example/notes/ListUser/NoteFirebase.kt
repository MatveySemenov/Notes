package com.example.notes.ListUser

class NoteFirebase() {
    var id: String = ""
    var title: String = ""
    var text: String = ""
    var date: String = ""

    // Добавьте конструктор без аргументов
    constructor(id: String, title: String, text: String, date: String) : this() {
        this.id = id
        this.title = title
        this.text = text
        this.date = date
    }
}