package com.example.todoapp.data.firebase

import com.example.todoapp.data.model.Todo
import com.example.todoapp.util.RequestResult
import kotlinx.coroutines.flow.Flow

interface TodoFirebaseActions {
    suspend fun getTodos(): Flow<RequestResult<List<Todo?>>>

    suspend fun insertTodo(todoFirebase: Todo) : Flow<RequestResult<String>>

    suspend fun updateTodo(todoFirebase: Todo) : Flow<RequestResult<String>>

    suspend fun deleteTodo(todoFirebase: Todo) : Flow<RequestResult<String>>

    suspend fun addWithId(todo: Todo) : Boolean

    suspend fun update(todo: Todo) : Boolean?

    suspend fun delete(todo: Todo) : Boolean?

    suspend fun deleteById(id: String) : Boolean
}