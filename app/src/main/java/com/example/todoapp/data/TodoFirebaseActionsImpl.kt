package com.example.todoapp.data

import com.example.todoapp.data.firebase.TodoFirebaseActions
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.util.IDGenerator
import com.example.todoapp.util.RequestResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TodoFirebaseActionsImpl(private val todosReference: DatabaseReference) : TodoFirebaseActions {

    override suspend fun getTodos() : Flow<RequestResult<List<Todo?>>> = callbackFlow {
//        trySend(FirebaseResult.Loading)
        todosReference.keepSynced(true)
        val event = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val todos = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Todo::class.java)
                    }
                    trySend(RequestResult.Success(todos))
                } catch (e : Exception) {
                    trySend(RequestResult.Error(e, emptyList()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(RequestResult.Error(Throwable(error.message), emptyList()))
            }
        }
        todosReference.addValueEventListener(event)
        awaitClose { close() }
    }

    override suspend fun insertTodo(todoFirebase: Todo) : Flow<RequestResult<String>> = callbackFlow {
        try {
            todoFirebase.validator()

            val todoId = IDGenerator().generateTodoId()
            val newTodo = Todo(id = todoId, text = todoFirebase.text, time = todoFirebase.time)
            todosReference.child(todoId).setValue(newTodo)
                .addOnSuccessListener { trySend(RequestResult.Success("Todo Added")) }
                .addOnFailureListener { trySend(RequestResult.Error(Throwable(it.message), "")) }
        } catch (e : java.lang.Exception) {
            trySend(RequestResult.Error(e, ""))
        }
        awaitClose { close()
        }
    }

    override suspend fun updateTodo(todoFirebase: Todo) : Flow<RequestResult<String>> = callbackFlow {
        try {
            todoFirebase.validator()

            todosReference.child(todoFirebase.id).updateChildren(todoFirebase.toMap())
                .addOnSuccessListener { trySend(RequestResult.Success("Todo updated")) }
                .addOnFailureListener { trySend(RequestResult.Error(Throwable(it.message), "")) }
        } catch (e : java.lang.Exception) {
            trySend(RequestResult.Error(e, ""))
        }
        awaitClose { close() }
    }

    override suspend fun deleteTodo(todoFirebase: Todo) : Flow<RequestResult<String>> = callbackFlow {
        try {
            todosReference.child(todoFirebase.id).removeValue()
                .addOnSuccessListener { trySend(RequestResult.Success("Todo deleted")) }
                .addOnFailureListener { trySend(RequestResult.Error(Throwable(it.message), "")) }
        } catch (e : java.lang.Exception) {
            trySend(RequestResult.Error(e, ""))
        }
        awaitClose { close() }
    }

    override suspend fun addWithId(todo: Todo): Boolean {
        return try {
            todo.validator()

            todosReference.child(todo.id).setValue(todo)
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    override suspend fun update(todo: Todo): Boolean? {
        return try {
            todo.validator()

            todosReference.child(todo.id).updateChildren(todo.toMap())
            true
        } catch (e : java.lang.Exception) {
            false
        }
    }

    override suspend fun delete(todo: Todo): Boolean? {
        return try {
            todosReference.child(todo.id).removeValue()
            true
        } catch (e : java.lang.Exception) {
            false
        }
    }

    override suspend fun deleteById(id: String): Boolean {
        return try {
            todosReference.child(id).removeValue()
            true
        } catch (e : java.lang.Exception) {
            false
        }
    }


}