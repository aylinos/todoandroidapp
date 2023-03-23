package com.example.todoapp.util

import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface ServiceHandlerActions {
    fun getAllTodosLocal() : Flow<List<Todo?>>
    suspend fun getAllTodosRemote() : Flow<RequestResult<List<Todo?>>>

    suspend fun syncTodos()

    //Local updates
    suspend fun createTodo(todo: Todo) : Flow<RequestResult<Todo?>>
    suspend fun updateTodo(todo: Todo) : Flow<RequestResult<Todo?>>
    suspend fun deleteTodo(todo: Todo) : Flow<RequestResult<Todo?>>

    //Remote updates
    suspend fun postCreateTodo(todo: Todo)
    suspend fun postUpdateTodo(todo: Todo)
    suspend fun postDeleteTodo(todo: Todo)
}