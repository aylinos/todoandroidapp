package com.example.todoapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Graph
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSourceContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

//Todo page
class DetailViewModel (
    private val todoDataSource: TodoDataSourceContract = Graph.todoRepo,
    private val id: Long,
    //use the id to access the concrete todo on click
) : ViewModel() {
    private val todoText = MutableStateFlow("")
    private val todoTime = MutableStateFlow("")
    private val selectId = MutableStateFlow(-1L)

    private val _state = MutableStateFlow(DetailViewState())
    val state: StateFlow<DetailViewState>
        get() = _state

    init {
        //fill in the values with the respective values for the passed todo
        viewModelScope.launch {
            combine(todoText, todoTime, selectId) { text, time, id ->
                DetailViewState(text, time, id)
            }.collect {
                _state.value = it
            }
        }
    }

    init{
        viewModelScope.launch {
            todoDataSource.getAllTodos()
                .collect {  //selectAll
                //get data if todo is completed or not
                it.find {
                    it.id == id
                }.also {
                    selectId.value = it?.id ?: -1
                    if (selectId.value != -1L) {
                        todoText.value = it?.todo ?: ""
                        todoTime.value = it?.time ?: ""
                        _state.value = DetailViewState(todoText.value, todoTime.value, id)
                    }
                }
            }

        }

    }

    fun onTextChange(newText: String) {
        todoText.value = newText
    }

    fun onTimeChange(newText: String) {
        todoTime.value = newText
    }

    fun insert(todo: Todo) = viewModelScope.launch {
        Log.d("DetailViewModel", "Input from detail screen on create new: ${todo.id}")
        todoDataSource.insertTodo(todo)
    }
}

//State
data class DetailViewState(
    val todo: String = "",
    val time: String = "",
    val selectId: Long = -1L,
)

//Takes the data received and passes is to the corresponding ViewModel of the given todo if exists
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val id: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id = id) as T
        } else {
            throw IllegalArgumentException("unKnown view model class")
        }
    }
}