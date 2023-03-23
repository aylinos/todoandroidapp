package com.example.todoapp.data.firebase

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.room.TodoDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

abstract class TodoRealtimeDatabase {
    companion object {
        @Volatile
        private var INSTANCE: DatabaseReference? = null
        fun getDatabaseReference(): DatabaseReference {
            return INSTANCE ?: synchronized(this) {
                val instance = Firebase.database("https://todoapp-9b0d8-default-rtdb.europe-west1.firebasedatabase.app/").reference
                INSTANCE = instance
                instance
            }
        }

        fun getDatabaseTodosReference() : DatabaseReference {
            return getDatabaseReference().child("todos")
        }
    }
}