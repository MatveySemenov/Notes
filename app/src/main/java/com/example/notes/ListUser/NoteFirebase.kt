package com.example.notes.ListUser

class NoteFirebase() {
    var title: String = ""
    var text: String = ""
    var date: String = ""

    // Добавьте конструктор без аргументов
    constructor(title: String, text: String, date: String) : this() {
        this.title = title
        this.text = text
        this.date = date
    }
}