package com.example.todoapp.ui.detail

import android.util.Log
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDataSourceContract
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

//    private val scheduler = TestCoroutineScheduler()
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
            listOf(todo, todo2)
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

    @Before
    fun setUp () {

    }

    @After
    fun tearDown() {

    }

//    @Mock
//    private lateinit var todoDataSource: TodoDataSourceContract
    private var todoDataSource: TodoDataSourceContract = MyFakeRepository()

//    @Test
    fun detailViewModel_NoIdProvided_EmptyPageLoaded()  {
        Mockito.doReturn(todos).`when`(todoDataSource).getAllTodos()

        val viewModel = createViewModelForCreate(todoDataSource)
//        val viewModel = DetailViewModel(todoDataSource, id = -1L)

        var currentDetailState = viewModel.state.value
        Truth.assertThat(currentDetailState.selectId).isEqualTo(-1L)
        Truth.assertThat(currentDetailState.todo).isEqualTo("")
        Truth.assertThat(currentDetailState.time).isEqualTo("")
    }

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val myScope = GlobalScope

    @Test
    fun coroutinesTest() = runTest {
            // ### PROBLEM ### --> test skips suspend fun in coroutinescope after todoDataSource.getAllTodos()

            val viewModel = DetailViewModel(todoDataSource, id = 1)

            var currentDetailState = viewModel.state.value
            println("#######$currentDetailState")

//        Truth.assertThat(currentDetailState.selectId).isEqualTo(1L)
    //        Mockito.`when`(todoDataSource.getAllTodos()).thenReturn(flow {listOf(todo, todo2)})
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

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
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

class MyFakeRepository : TodoDataSourceContract {
    private val todo: Todo = Todo("Fake todo", "13pm", id = 1)
    private val todo2: Todo = Todo("Fake 2", "12am", id = 2)
    override fun getAllTodos(): Flow<List<Todo>> {
        return flow {
            listOf(todo, todo2)
        }
    }

    override suspend fun insertTodo(todo: Todo) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodo(todo: Todo) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTodo(isCompleted: Boolean, id: Long) {
        TODO("Not yet implemented")
    }
}