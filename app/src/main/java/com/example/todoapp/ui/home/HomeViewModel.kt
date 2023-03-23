package com.example.todoapp.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Graph
import com.example.todoapp.data.model.Todo
import com.example.todoapp.util.RequestResult
import com.example.todoapp.util.ServiceHandler
import com.example.todoapp.util.ServiceHandlerActions
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class HomeViewModel(
    private val serviceHandler: ServiceHandlerActions = Graph.serviceHandler
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    private val selected = MutableStateFlow(_state.value.selected) //for isComplete property

    init {
        getLocalTodosAndCollectState()
    }

    //Functions to update this state

    private fun getLocalTodosAndCollectState() = viewModelScope.launch {
        val todoList = serviceHandler.getAllTodosLocal()
        combine(todoList, selected) { todoList: List<Todo?>, selected: Boolean ->
            HomeViewState(todoList, selected)
        }.collect {//collect the recent values from the HomeViewModel, they are provided with StateFlow
            _state.value = it
        }
    }

    fun updateTodo(todoFirebase: Todo, selected: Boolean) = viewModelScope.launch {
        val todoToUpdate = Todo(id = todoFirebase.id, text = todoFirebase.text, time = todoFirebase.time, complete = selected)
        serviceHandler.updateTodo(todoToUpdate).collect {result ->
            when(result) {
                is RequestResult.Success -> {
                    result.data?.let { serviceHandler.postUpdateTodo(it) }
                }
                is RequestResult.Error -> {
                    println(result.exception.message)
//                    _state.update { it.copy(todoList = emptyList(), isLoading = false, errorMsg = result.exception.message) }
                }
                is RequestResult.Loading<*> -> {
                }
            }
        }
    }

    fun deleteTodo(todoFirebase: Todo) = viewModelScope.launch {
        serviceHandler.deleteTodo(todoFirebase).collect {result ->
            when(result) {
                is RequestResult.Success -> {
                    result.data?.let { serviceHandler.postDeleteTodo(it) }
                }
                is RequestResult.Error -> {
                    println(result.exception.message)
                }
                is RequestResult.Loading<*> -> {
                }
            }
        }
    }
}

data class HomeViewState(
    val todoList: List<Todo?> = emptyList(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val selected: Boolean = false,
)