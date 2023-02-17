package com.example.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Graph
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

//Home page
class HomeViewModel(private val todoDataSource: TodoDataSource = Graph.todoRepo) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

//    val todoList = todoDataSource.getAllTodos() //selectAll
    val selected = MutableStateFlow(_state.value.selected)

    init {
        viewModelScope.launch {
            val todoList = todoDataSource.getAllTodos()
            combine(todoList, selected) { todoList: List<Todo>, selected: Boolean ->
                HomeViewState(todoList, selected)
            }.collect {//collect the recent values from the HomeViewModel, they are provided with StateFlow
                _state.value = it
            }
        }
    }

    //Functions to update this state

    fun updateTodo(selected: Boolean, id: Long) = viewModelScope.launch {
        todoDataSource.updateTodo(selected, id)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        todoDataSource.deleteTodo(todo)
    }
}

data class HomeViewState(
    val todoList: List<Todo> = emptyList(),
    val selected: Boolean = false,
)