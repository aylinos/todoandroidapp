package com.example.todoapp

import android.content.Context
import com.example.todoapp.data.TodoDataSource
import com.example.todoapp.data.room.TodoDatabase

object Graph {
    private lateinit var database: TodoDatabase

    val todoRepo by lazy {
        TodoDataSource(database.todoDao())
    }

    fun initialize(context: Context) {
        database = TodoDatabase.getDatabase(context)
    }
}