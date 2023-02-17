package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

interface TodoDataSourceContract {
    fun getAllTodos(): Flow<List<Todo>>

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun updateTodo(isCompleted: Boolean, id: Long)
}