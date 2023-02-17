package com.example.todoapp.data

import com.example.todoapp.data.room.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

//The repository where data can be fetched, use to access the todos
open class TodoDataSource(private val todoDao: TodoDao) : TodoDataSourceContract {
//    val selectAll = todoDao.selectAll()

     override fun getAllTodos(): Flow<List<Todo>> { //flow represents asynchronous data stream
         //If you are using both suspend and livedata in one function signature you are just handling async operation twice and making excess headache for yourself-
        Dispatchers.IO.apply {
            return todoDao.selectAll()
        }
    }

    override suspend fun insertTodo(todo: Todo) {
        Dispatchers.IO.apply {
            todoDao.insert(todo)
        }
    }

    override suspend fun deleteTodo(todo: Todo) {
        Dispatchers.IO.apply {
            todoDao.delete(todo.id)
        }
    }

    override suspend fun updateTodo(isCompleted: Boolean, id: Long) {
        Dispatchers.IO.apply {
            todoDao.updateComplTodo(isCompleted, id)
        }
    }
}