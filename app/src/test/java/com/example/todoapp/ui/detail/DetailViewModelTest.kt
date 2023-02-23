package com.example.todoapp.ui.detail

import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSourceContract
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DetailViewModelTest {

    companion object {
        private val todo: Todo = Todo("Fake todo", "13pm", id = 1)
        private val todo2: Todo = Todo("Fake 2", "12am", id = 2)
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
        id: Long = todo.id,
    ) = DetailViewModel(
        todoDataSource = todoDataSource,
        id = id
    )

    @Before
    fun setUp () {

    }

    @After
    fun tearDown() {

    }

    private var fakeTodoDataSource: TodoDataSourceContract = MyFakeRepository(listOf(todo, todo2))

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun detailViewModel_NoIdProvided_EmptyPageLoaded()  {
        val viewModel = createViewModelForCreate(fakeTodoDataSource)

        var currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        Truth.assertThat(currentDetailState.selectId).isEqualTo(-1L)
        Truth.assertThat(currentDetailState.todo).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }

    @Test
    fun detailViewModel_IdProvided_FilledPageLoaded()  {
        val viewModel = DetailViewModel(fakeTodoDataSource, id = todo.id)

        var currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        Truth.assertThat(currentDetailState.selectId).isEqualTo(todo.id)
        Truth.assertThat(currentDetailState.todo).isEqualTo(todo.todo)
        Truth.assertThat(currentDetailState.time).isEqualTo(todo.time)
    }

    @Test
    fun detailViewModel_TimePropertyChanged_StateUpdated()  {
        val viewModel = DetailViewModel(fakeTodoDataSource, id = todo.id)

        var currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        viewModel.onTimeChange("${todo.time}1")
        currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        Truth.assertThat(currentDetailState.selectId).isEqualTo(1L)
        Truth.assertThat(currentDetailState.todo).isEqualTo(todo.todo)
        Truth.assertThat(currentDetailState.time).isEqualTo("${todo.time}1")
    }

    @Test
    fun detailViewModel_TextPropertyChanged_StateUpdated()  {
        val viewModel = DetailViewModel(fakeTodoDataSource, id = todo.id)

        var currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        viewModel.onTextChange("${todo.todo}o")
        currentDetailState = viewModel.state.value
        println("#######$currentDetailState")

        Truth.assertThat(currentDetailState.selectId).isEqualTo(todo.id)
        Truth.assertThat(currentDetailState.todo).isEqualTo("${todo.todo}o")
        Truth.assertThat(currentDetailState.time).isEqualTo(todo.time)
    }

    @Test
    fun detailViewModel_InsertTodo_SubMethodCalled()  {
        val formedTodo = Todo("New todo", "1am")

        val viewModel = createViewModelForCreate(fakeTodoDataSource)
        val calledMethod = viewModel.insert(formedTodo)

        Truth.assertThat(calledMethod.isCompleted).isTrue()
    }
}

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

class MyFakeRepository(private var todos: List<Todo>) : TodoDataSourceContract {

    override fun getAllTodos(): Flow<List<Todo>> = flow {
        emit(todos)
//        for(i in listOf(todos)) {
//            emit(i)
//        }
    }

    override suspend fun insertTodo(todo: Todo) {
        println("Todo with id=${todo.id} was received")
    }

    override suspend fun deleteTodo(todo: Todo) {
        println("Todo with id=${todo.id} was received")
    }

    override suspend fun updateTodo(isCompleted: Boolean, id: Long) {
        println("Todo with id=$id and status=$isCompleted was received")
    }
}