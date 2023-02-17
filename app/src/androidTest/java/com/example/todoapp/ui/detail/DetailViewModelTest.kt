package com.example.todoapp.ui.detail

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSource
import com.example.todoapp.data.TodoDataSourceContract
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    //Steps
    //1. Create a companion object with fake state for both create and update + list of all todos
    //2. Initialize the view model class - 2 scenarios (with and without provided id)
    //3. Mock the actions in init --> getAllTodos()
    //4. Test whether the state of view model is as expected in both scenarios
    //5. Test fun insert (both scenarios update and create)

    companion object {
        private val todo: Todo = Todo("Fake todo", "13pm", id = 1)
        private val todo2: Todo = Todo("Fake 2", "12am", id = 2)
        val todos: Flow<List<Todo>> = flow {
//            listOf<Todo>(Todo("Fake todo", "13pm", id = 1), Todo("Fake 2", "12am", id = 2))
            listOf<Todo>(todo, todo2)
        }

        val fakeStateCreate = DetailViewState(todo = "", time = "", selectId = -1L)
        val fakeStateUpdate = DetailViewState(todo = todo.todo, time = todo.time, selectId = todo.id)
    }

    private fun createViewModelForCreate(
        todoDataSource: TodoDataSourceContract,
        id: Long = -1L
    ) = DetailViewModel(
        todoDataSource = todoDataSource,
        id = id
    )

    private fun createViewModelForUpdate(
        todoDataSource: TodoDataSourceContract,
        id: Long = todo.id
    ) = DetailViewModel(
        todoDataSource = todoDataSource,
        id = id
    )

    @Mock
    private lateinit var todoDataSource: TodoDataSource

    @Test
    fun detailViewModel_NoIdProvided_EmptyPageLoaded()  {
        Mockito.doReturn(todos).`when`(todoDataSource).getAllTodos()

        val viewModel = createViewModelForCreate(todoDataSource)
//        val viewModel = DetailViewModel(todoDataSource, id = -1L)

        var currentDetailState = viewModel.state.value
        Truth.assertThat(currentDetailState.selectId).isEqualTo(-1L)
        Truth.assertThat(currentDetailState.todo).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun coroutinesTest() = runTest {
        val viewModel = DetailViewModel(todoDataSource, id = 1)
        viewModel.viewModelScope.launch{

        }
    }

//    @Test
    fun detailViewModel_IdProvided_FilledPageLoaded()  {
        val viewModel = DetailViewModel(todoDataSource, id = 1)



        val list = Mockito.`when`(todoDataSource.getAllTodos()).thenReturn(todos)
        Log.d("DetailViewModelTest", "List: ${list.toString()}")
        //list is mocked

//        val viewModel = createViewModelForUpdate(todoDataSource)


        var currentDetailState = viewModel.state.value
        Truth.assertThat(currentDetailState.selectId).isEqualTo(-1L)
        Truth.assertThat(currentDetailState.todo).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }
}