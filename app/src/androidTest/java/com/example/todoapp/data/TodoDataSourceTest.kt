package com.example.todoapp.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoapp.data.room.TodoDao
import com.example.todoapp.data.room.TodoDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoDataSourceTest {
    private lateinit var todoDao: TodoDao
    private lateinit var db: TodoDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        todoDao = db.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertTodoAndGetInList() = runBlocking {
        val todo = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList[0].todo).isEqualTo(todo.todo)
        assertThat(todosList[0].time).isEqualTo(todo.time)
        assertThat(todosList[0].isComplete).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val todo1 = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo1)
        val todo2 = Todo("clean floors", "13:00pm")
        todoDao.insert(todo2)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList[0].todo).isEqualTo(todo1.todo)
        assertThat(todosList[1].todo).isEqualTo(todo2.todo)
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneTodo() = runBlocking {
        val todo1 = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo1)
        val todo2 = Todo("clean floors", "13:00pm")
        todoDao.insert(todo2)
        todoDao.delete(1)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList).hasSize(1)
        assertThat(todosList[0].id).isEqualTo(2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val todo1 = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo1)
        val todo2 = Todo("clean floors", "13:00pm")
        todoDao.insert(todo2)
        todoDao.deleteAllTodo()
        val todosList = todoDao.selectAll().first()
        assertThat(todosList).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun updateComplTodo() = runBlocking {
        var todo = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo)
        todo = todoDao.selectAll().first()[0]
        todoDao.updateComplTodo(true,todo.id)
        val updatedTodo = todoDao.selectAll().first()[0]
        assertThat(updatedTodo.isComplete).isTrue()
        assertThat(updatedTodo.todo).isEqualTo(todo.todo)
        assertThat(updatedTodo.time).isEqualTo(todo.time)
    }

    @Test
    @Throws(Exception::class)
    fun updateTodo() = runBlocking {
        var todo = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo)
        todo = todoDao.selectAll().first()[0]
        todo = Todo("updated", "12:45pm", id = todo.id)
        todoDao.insert(todo)
        val updatedTodo = todoDao.selectAll().first()[0]
        assertThat(updatedTodo.isComplete).isFalse()
        assertThat(updatedTodo.todo).isEqualTo("updated")
        assertThat(updatedTodo.time).isEqualTo(todo.time)
    }
}