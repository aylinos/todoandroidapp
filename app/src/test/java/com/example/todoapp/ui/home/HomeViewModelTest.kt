package com.example.todoapp.ui.home

import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.util.IDGenerator
import com.example.todoapp.ui.detail.CoroutinesTestRule
import com.example.todoapp.util.RequestResult
import com.example.todoapp.util.ServiceHandlerActions
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    companion object {
        private val todo: Todo = Todo("Fake todo", "13pm", id = IDGenerator().generateTodoId())
        private val todo2: Todo = Todo("Fake 2", "12am", id = IDGenerator().generateTodoId())
        private val fakeTodoList: List<Todo> = listOf(todo, todo2)
    }

    private var fakeServiceHandler: ServiceHandlerActions = Mockito.spy(ServiceHandlerActions::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun homeViewModel_TodoItemsProvided_ItemsLoadedOnPage()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = HomeViewModel(fakeServiceHandler)
        val currentHomeState = viewModel.state.value

        Truth.assertThat(currentHomeState.selected).isFalse()
        Truth.assertThat(currentHomeState.todoList).isEqualTo(fakeTodoList)
    }

    @Test
    fun homeViewModel_NoTodoItemsProvided_EmptyPageLoaded()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(emptyList()) })

        val viewModel = HomeViewModel(fakeServiceHandler)
        val currentHomeState = viewModel.state.value

        Truth.assertThat(currentHomeState.selected).isFalse()
        Truth.assertThat(currentHomeState.todoList).isEmpty()
    }

    @Test
    fun homeViewModel_UpdateTodo_SubMethodCalled(): Unit = runBlocking {
        val formedTodo = Todo(id = todo.id, text = todo.text, time = todo.time, complete = true)

        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })
        Mockito.`when`(fakeServiceHandler.updateTodo(formedTodo)).thenReturn(flow { RequestResult.Success(todo) })

        val viewModel = HomeViewModel(fakeServiceHandler)
        viewModel.updateTodo(todo, true)

        Mockito.verify(fakeServiceHandler).updateTodo(formedTodo)
    }

    @Test
    fun homeViewModel_DeleteTodo_SubMethodCalled(): Unit = runBlocking {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })
        Mockito.`when`(fakeServiceHandler.deleteTodo(todo)).thenReturn(flow { RequestResult.Success(todo) })

        val viewModel = HomeViewModel(fakeServiceHandler)
        viewModel.deleteTodo(todo)

        Mockito.verify(fakeServiceHandler).deleteTodo(todo)
    }
}