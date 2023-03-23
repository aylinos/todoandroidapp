package com.example.todoapp.ui.detail

import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.util.IDGenerator
import com.example.todoapp.util.RequestResult
import com.example.todoapp.util.ServiceHandlerActions
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DetailViewModelTest {

    companion object {
        private val todo: Todo = Todo("Fake todo", "13pm", id = IDGenerator().generateTodoId())
        private val todo2: Todo = Todo("Fake 2", "12am", id = IDGenerator().generateTodoId())
        private val fakeTodoList: List<Todo> = listOf(todo, todo2)
    }

    @Before
    fun setUp () {
    }

    @After
    fun tearDown() {

    }

    private var fakeServiceHandler: ServiceHandlerActions = Mockito.spy(ServiceHandlerActions::class.java)

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun detailViewModel_NoIdProvided_EmptyPageLoaded()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = DetailViewModel(fakeServiceHandler, "-1")
        var currentDetailState = viewModel.state.value

        Truth.assertThat(currentDetailState.selectId).isEqualTo("-1")
        Truth.assertThat(currentDetailState.text).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }

    @Test
    fun detailViewModel_IdProvided_FilledPageLoaded()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = DetailViewModel(fakeServiceHandler, id = todo.id)
        var currentDetailState = viewModel.state.value

        Truth.assertThat(currentDetailState.selectId).isEqualTo(todo.id)
        Truth.assertThat(currentDetailState.text).isEqualTo(todo.text)
        Truth.assertThat(currentDetailState.time).isEqualTo(todo.time)
    }

    @Test
    fun detailViewModel_NonExistentIdProvided_EmptyPageLoaded()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = DetailViewModel(fakeServiceHandler, id = "1234")
        var currentDetailState = viewModel.state.value

        Truth.assertThat(currentDetailState.selectId).isEqualTo("-1")
        Truth.assertThat(currentDetailState.text).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }

    @Test
    fun detailViewModel_TimePropertyChanged_StateUpdated()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = DetailViewModel(fakeServiceHandler, id = todo.id)
        viewModel.onTimeChange("${todo.time}1")
        val currentDetailState = viewModel.state.value

        Truth.assertThat(currentDetailState.selectId).isEqualTo(todo.id)
        Truth.assertThat(currentDetailState.text).isEqualTo(todo.text)
        Truth.assertThat(currentDetailState.time).isEqualTo("${todo.time}1")
    }

    @Test
    fun detailViewModel_TextPropertyChanged_StateUpdated()  {
        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })

        val viewModel = DetailViewModel(fakeServiceHandler, id = todo.id)
        viewModel.onTextChange("${todo.text}o")
        val currentDetailState = viewModel.state.value

        Truth.assertThat(currentDetailState.selectId).isEqualTo(todo.id)
        Truth.assertThat(currentDetailState.text).isEqualTo("${todo.text}o")
        Truth.assertThat(currentDetailState.time).isEqualTo("${todo.time}")
    }

    @Test
    fun detailViewModel_InsertTodo_SubMethodCalled(): Unit = runBlocking {
        val formedTodo = Todo("New todo", "1am", id = IDGenerator().generateTodoId())

        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })
        Mockito.`when`(fakeServiceHandler.createTodo(formedTodo)).thenReturn(flow {RequestResult.Success(formedTodo)})

        val viewModel = DetailViewModel(fakeServiceHandler, "-1")
        viewModel.insertTodo(formedTodo)

        Mockito.verify(fakeServiceHandler).createTodo(formedTodo)
    }

    @Test
    fun detailViewModel_UpdateTodo_SubMethodCalled(): Unit = runBlocking {
        val todoToUpdate = Todo(id = todo.id, text = "update", time = todo.time, complete = todo.complete)

        Mockito.`when`(fakeServiceHandler.getAllTodosLocal()).thenReturn(flow { emit(fakeTodoList) })
        Mockito.`when`(fakeServiceHandler.updateTodo(todoToUpdate)).thenReturn(flow {RequestResult.Success(todoToUpdate)})

        val viewModel = DetailViewModel(fakeServiceHandler, todo.id)
        viewModel.updateTodo(todoToUpdate)

        Mockito.verify(fakeServiceHandler).updateTodo(todoToUpdate)
    }
}

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}