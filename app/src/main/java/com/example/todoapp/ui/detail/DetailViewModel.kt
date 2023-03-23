package com.example.todoapp.ui.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Graph
import com.example.todoapp.data.model.Todo
import com.example.todoapp.util.RequestResult
import com.example.todoapp.util.ServiceHandlerActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailViewModel(
    private val serviceHandler: ServiceHandlerActions = Graph.serviceHandler,
    private val id: String,
    //use the id to access the concrete todo on click
) : ViewModel() {

    private val todoText = MutableStateFlow("")
    private val todoTime = MutableStateFlow("")
    private val selectId = MutableStateFlow("-1")

    private val _state = MutableStateFlow(DetailViewState())
    val state: StateFlow<DetailViewState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(todoText, todoTime, selectId) { text, time, id ->
                DetailViewState(text, time, id)
            }.collect {
                _state.value = it
            }
        }
    }

    init {
        getTodos()
    }

    fun onTextChange(newText: String) {
        todoText.value = newText
    }

    fun onTimeChange(newText: String) {
        todoTime.value = newText
    }

    private fun getTodos() = viewModelScope.launch {
        val todoList = serviceHandler.getAllTodosLocal().first()
        todoList.find {
            if(it != null) {
                it.id == id
            } else { false }
        }.also {
            selectId.value = it?.id ?: "-1"
            if (selectId.value != "-1") {
                todoText.value = it?.text ?: ""
                todoTime.value = it?.time ?: ""
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun insertTodo(todo: Todo) = viewModelScope.launch {
        serviceHandler.createTodo(todo).collect { result ->
            when(result) {
                is RequestResult.Success -> {
                    println(result.data);
                    result.data?.let { serviceHandler.postCreateTodo(it) }
                }
                is RequestResult.Error -> {
                    println(result.exception.message)
                }
                is RequestResult.Loading<*> -> {
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateTodo(todo: Todo) = viewModelScope.launch {
        val todoToUpdate = Todo(id = todo.id, text = todo.text, time = todo.time, complete = todo.complete)
        serviceHandler.updateTodo(todoToUpdate).collect { result ->
            when(result) {
                is RequestResult.Success -> {
                    result.data?.let { serviceHandler.postUpdateTodo(it) }
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

//State
data class DetailViewState(
    val text: String = "",
    val time: String = "",
    val selectId: String = "-1",
)

//Takes the data received and passes is to the corresponding ViewModel of the given todo if exists
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id = id) as T
        } else {
            throw IllegalArgumentException("unKnown view model class")
        }
    }
}