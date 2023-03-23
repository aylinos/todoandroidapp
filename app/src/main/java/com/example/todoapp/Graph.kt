package com.example.todoapp

import android.content.Context
import com.example.todoapp.data.TodoDataSource
import com.example.todoapp.data.TodoFirebaseActionsImpl
import com.example.todoapp.data.TodoLocalStatusActionsImpl
import com.example.todoapp.data.firebase.TodoRealtimeDatabase
import com.example.todoapp.data.room.TodoDatabase
import com.example.todoapp.util.ServiceHandler
import com.google.firebase.database.DatabaseReference

object Graph {
    private lateinit var database: TodoDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseTodosReference: DatabaseReference

    val todoRepo by lazy {
        TodoDataSource(database.todoDao())
    }

    val todoActions by lazy {
        TodoFirebaseActionsImpl(databaseTodosReference)
    }

    val todoLocalStatusActions by lazy {
        TodoLocalStatusActionsImpl(database.todoLocalStatusDao())
    }

    val serviceHandler by lazy {
        ServiceHandler()
    }

    fun initialize(context: Context) {
        database = TodoDatabase.getDatabase(context)
        databaseReference = TodoRealtimeDatabase.getDatabaseReference()
        databaseTodosReference = TodoRealtimeDatabase.getDatabaseTodosReference()
    }
}