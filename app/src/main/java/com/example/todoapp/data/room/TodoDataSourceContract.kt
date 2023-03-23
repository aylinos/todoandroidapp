package com.example.todoapp.data.room

import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoDataSourceContract {
    fun getAllTodos(): Flow<List<Todo>>

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: String): Todo?
}