package com.example.todoapp.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.todoapp.Graph
import com.example.todoapp.data.*
import com.example.todoapp.data.firebase.TodoFirebaseActions
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.model.TodoLocalStatus
import com.example.todoapp.data.room.TodoDataSourceContract
import com.example.todoapp.data.room.TodoLocalStatusActions
import com.example.todoapp.data.util.IDGenerator
import com.example.todoapp.data.util.StatusType
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class ServiceHandler (
    private val roomActions : TodoDataSourceContract = Graph.todoRepo,
    private val remoteActions : TodoFirebaseActions = Graph.todoActions,
    private val todoLocalStatusActions: TodoLocalStatusActions = Graph.todoLocalStatusActions
) : ServiceHandlerActions {

    override fun getAllTodosLocal() : Flow<List<Todo?>> {
        return roomActions.getAllTodos()
    }

    override suspend fun getAllTodosRemote() : Flow<RequestResult<List<Todo?>>> {
        return remoteActions.getTodos()
    }

    override suspend fun syncTodos() {
        syncFromLocalToFirebase()
        synchronizeFromFirebaseToLocal()
    }

    override suspend fun createTodo(todo: Todo) : Flow<RequestResult<Todo?>> = callbackFlow {
        //add new todo to the local db
        try {
            todo.validator()

            val id: String = IDGenerator().generateTodoId()
            val newTodo = Todo(id = id, text = todo.text, time = todo.time)
            roomActions.insertTodo(newTodo)
            trySend(RequestResult.Success(newTodo))
        } catch (e: Exception) {
            trySend(RequestResult.Error(e, null))
        }
        awaitClose { close() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun postCreateTodo(todo: Todo) {
        Dispatchers.IO.apply {
            //Check for internet connection
            if(NetworkConnectionManager.isOnline()) {
                try {
                    //If yes -> directly add the new todo in firebase
                    remoteActions.addWithId(todo)
                } catch (e: Exception) {
                    Log.d("POST CREATE TODO", e.message ?: "Exception: $e")
                }
            } else {
                //If no -> add reference of the new todo to the additional table with status CREATED
                todoLocalStatusActions.insert(TodoLocalStatus(todo.id, StatusType.CREATED))
            }
        }
    }

    override suspend fun updateTodo(todo: Todo) : Flow<RequestResult<Todo?>> = callbackFlow {
        //update the todo in the local db
        try {
            todo.validator()

            roomActions.insertTodo(todo)
            trySend(RequestResult.Success(todo))
        } catch (e: Exception) {
            trySend(RequestResult.Error(e, null))
        }
        awaitClose { close() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun postUpdateTodo(todo: Todo) {
        //separate coroutine (dispatcher)
        Dispatchers.IO.apply {
            //Check for internet connection
            if(NetworkConnectionManager.isOnline()) {
                try {
                    //If yes -> directly update the todo in firebase
                    remoteActions.update(todo)
                } catch (e: Exception) {
                    Log.d("POST UPDATE TODO", e.message ?: "Exception: $e")
                }
            } else {
                //If no -> add reference of the updated todo to the additional table with status UPDATED
                todoLocalStatusActions.insert(TodoLocalStatus(todo.id, StatusType.UPDATED))
            }
        }
    }

    override suspend fun deleteTodo(todo: Todo) : Flow<RequestResult<Todo?>> = callbackFlow {
        //delete the todo in the local db
        try {
            roomActions.deleteTodo(todo)
            trySend(RequestResult.Success(todo))
        } catch (e: Exception) {
            trySend(RequestResult.Error(e, null))
        }
        awaitClose { close() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun postDeleteTodo(todo: Todo) {
        //separate coroutine (dispatcher)
        Dispatchers.IO.apply {
            //Check for internet connection
            if(NetworkConnectionManager.isOnline()) {
                try {
                    //If yes -> directly delete the todo in firebase
                    remoteActions.delete(todo)
                } catch (e: Exception) {
                    Log.d("POST DELETE TODO", e.message ?: "Exception: $e")
                }
            } else {
                //TODO -> check if there are previous lines with the same reference and delete all of them
                //If no -> add reference of the deleted todo to the additional table with status DELETED
                todoLocalStatusActions.insert(TodoLocalStatus(todo.id, StatusType.DELETED))
            }
        }
    }

    private suspend fun syncFromLocalToFirebase() {
            //TODO: if element exists locally but not in firebase and it is not in the additional table -> delete it from the local db

            //1. get all rows from the additional table
            val unsynced: List<TodoLocalStatus> = todoLocalStatusActions.select()

            //2. if there are rows -> start updating firebase with them one by one and delete the finished rows locally
            if (unsynced.isNotEmpty()) {
                for (element in unsynced) {
                    val todo: Todo? = roomActions.getTodoById(element.reference)
                    var isSynced = false
                    if (todo != null) {
                        if(element.status == StatusType.CREATED) {
                            isSynced = remoteActions.addWithId(todo) == true
                        } else if(element.status == StatusType.UPDATED) {
                            isSynced = remoteActions.update(todo) == true
                        }
                    } else {
                        if (element.status == StatusType.DELETED) {
                            isSynced = remoteActions.deleteById(element.reference)
                        }
                    }
                    if (isSynced) {
                        //remove this row from the additional table
                        todoLocalStatusActions.delete(element.id)
                    }
                }
            }
    }

    private suspend fun synchronizeFromFirebaseToLocal() {
        //3. get all rows from firebase
        getAllTodosRemote().collect { result ->
            when(result) {
                is RequestResult.Success -> {
                    //4. iterate them one by one & check whether each todo exists locally
                    if(result.data.isNotEmpty()) {
                        for(element in result.data) {
                            if(element != null) {
                                val foundLocalTodo = element.let { roomActions.getTodoById(it.id) }
                                if (foundLocalTodo != null) {
                                    //5. if yes -> override with firebase data on differences = update
                                    roomActions.insertTodo(element)
                                } else {
                                    //6. if not -> add the todo in the local db
                                    element.validator()
                                    roomActions.insertTodo(element)
                                }
                            }
                        }
                    }
                }
                else -> {
                    Log.d("SYNCHRONIZE FIREBASE: ", "$result")
                }
            }
        }
    }
}