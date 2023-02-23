package com.example.todoapp.ui.home

import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSourceContract
import com.example.todoapp.ui.detail.CoroutinesTestRule
import com.example.todoapp.ui.detail.MyFakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    companion object {
        private val todo: Todo = Todo("Fake todo", "13pm", id = 1)
        private val todo2: Todo = Todo("Fake 2", "12am", id = 2)
        private val todos: List<Todo> = listOf(todo, todo2)
    }

    private var fakeTodoDataSourceWithItems: TodoDataSourceContract = MyFakeRepository(todos)
    private var fakeTodoDataSourceNoItems: TodoDataSourceContract = MyFakeRepository(listOf())

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun homeViewModel_TodoItemsProvided_ItemsLoadedOnPage()  {
        val viewModel = HomeViewModel(fakeTodoDataSourceWithItems)

        var currentHomeState = viewModel.state.value
        println("#######$currentHomeState")

        Truth.assertThat(currentHomeState.selected).isFalse()
        Truth.assertThat(currentHomeState.todoList).isEqualTo(todos)
    }

    @Test
    fun homeViewModel_NoTodoItemsProvided_EmptyPageLoaded()  {
        val viewModel = HomeViewModel(fakeTodoDataSourceNoItems)

        var currentHomeState = viewModel.state.value
        println("#######$currentHomeState")

        Truth.assertThat(currentHomeState.selected).isFalse()
        Truth.assertThat(currentHomeState.todoList).isEmpty()
    }

    @Test
    fun homeViewModel_UpdateTodo_SubMethodCalled()  {
        val viewModel = HomeViewModel(fakeTodoDataSourceWithItems)
        val calledMethod = viewModel.updateTodo(true, todo.id)

        Truth.assertThat(calledMethod.isCompleted).isTrue()
    }

    @Test
    fun homeViewModel_DeleteTodo_SubMethodCalled()  {
        val viewModel = HomeViewModel(fakeTodoDataSourceWithItems)
        val calledMethod = viewModel.delete(todo)

        Truth.assertThat(calledMethod.isCompleted).isTrue()
    }
}