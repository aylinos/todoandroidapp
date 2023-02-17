package com.example.todoapp.ui.detail

import com.example.todoapp.Graph
import com.example.todoapp.data.TodoDataSourceContract
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class DetailViewModelTest_1 {
    //Steps
    //1. Test initial state of DetailViewModel when creating a new todo
    //2. Test initial state of DetailViewModel when updating an existing todo
    //3. Fake todoDataSource.getAll() -> test both exists and doesn't exist
    //4. Fake todoDataSource.insert() -> test both create and update

    private lateinit var viewModel: DetailViewModel

    @Mock
//    lateinit var todoDataSource: TodoDataSourceContract
    val todoDataSource: TodoDataSourceContract = Graph.todoRepo

    @Test
    fun detailViewModel_NoIdProvided_EmptyPageLoaded()  {
//        val todoDataSource = TodoDataSource(todoDatabase.todoDao())

        Mockito.`when`(todoDataSource.getAllTodos()).thenReturn(emptyFlow())
        viewModel = DetailViewModel(id = -1L)

//        Mockito.doReturn()

        var currentDetailState = viewModel.state.value
        assertThat(currentDetailState.selectId).isEqualTo(-1L)
        assertThat(currentDetailState.todo).isEqualTo("")
        assertThat(currentDetailState.time).isEqualTo("")
    }

    @Test
    fun detailViewModel_IdProvided_FilledPageLoaded()  {
//        val todoDataSource = TodoDataSource(todoDatabase.todoDao())
//        viewModel = DetailViewModel(todoDataSource, 1)
//        viewModel.insert(Todo("wash dishes", "12:30pm"))

        var currentDetailState = viewModel.state.value
        assertThat(currentDetailState.selectId).isEqualTo(1L)
//        assertThat(currentDetailState.todo).isNotEqualTo("")
//        assertThat(currentDetailState.time).isNotEqualTo("")
    }
}