package com.example.todoapp.data

import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.room.TodoDao
import com.example.todoapp.data.room.TodoDataSourceContract
import com.example.todoapp.data.util.IDGenerator
//import com.example.todoapp.data.room.replaceAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

//The repository where data can be fetched, use to access the todos
//+ This class will have the business logic for fetching the data and caching it for offline access.
open class TodoDataSource(private val todoDao: TodoDao) : TodoDataSourceContract {

     override fun getAllTodos(): Flow<List<Todo>> { //flow represents asynchronous data stream
         //If you are using both suspend and livedata in one function signature you are just handling async operation twice and making excess headache for yourself-
        Dispatchers.IO.apply {
            return todoDao.selectAll()
        }
    }

    override suspend fun insertTodo(todo: Todo) {
        Dispatchers.IO.apply {
            var newTodo = todo
            if(todo.id == "") {
                val uuid = IDGenerator().generateTodoId()
                newTodo = Todo(text = todo.text, time = todo.time, id = uuid)
            }
            todoDao.insert(newTodo)
        }
    }

    override suspend fun deleteTodo(todo: Todo) {
        Dispatchers.IO.apply {
            todoDao.delete(todo.id)
        }
    }

    override suspend fun getTodoById(id: String): Todo? {
        Dispatchers.IO.apply {
            return todoDao.getById(id)
        }
    }
}