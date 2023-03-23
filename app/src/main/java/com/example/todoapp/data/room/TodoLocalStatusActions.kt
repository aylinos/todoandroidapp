package com.example.todoapp.data.room

import com.example.todoapp.data.model.TodoLocalStatus
import kotlinx.coroutines.flow.Flow

interface TodoLocalStatusActions {
    fun selectAll() : Flow<List<TodoLocalStatus>>

    suspend fun select() : List<TodoLocalStatus>

    suspend fun getAll() : List<TodoLocalStatus?>

    suspend fun getById(id: Long) : TodoLocalStatus?

    suspend fun getByReference(reference: String) : List<TodoLocalStatus>

    suspend fun insert(element: TodoLocalStatus)

    suspend fun insertMany(elements: List<TodoLocalStatus>)

    suspend fun update(element: TodoLocalStatus)

    suspend fun delete(id: Long)

    suspend fun deleteAll()
}