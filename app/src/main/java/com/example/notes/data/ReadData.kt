package com.example.notes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class User(val userName: String? = null , val userEmail: String? = null)

class ReadData {
    fun readUserDataByEmail(userEmailToSearch: String, callback: (List<User>) -> Unit) {
        // Получение ссылки на базу данных
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")

        // Поиск пользователя по адресу электронной почты
        usersRef.orderByChild("userEmail").equalTo(userEmailToSearch).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersList = mutableListOf<User>()

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        // Получение данных пользователя
                        val user = userSnapshot.getValue(User::class.java)
                        user?.let { usersList.add(it) }
                    }
                }
                callback(usersList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
