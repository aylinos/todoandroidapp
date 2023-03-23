package com.example.todoapp.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.room.TodoDao
import com.example.todoapp.data.room.TodoDatabase
import com.example.todoapp.data.util.IDGenerator
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
        val id: String = IDGenerator().generateTodoId()
        val todo = Todo(id = id, text ="wash dishes", time = "12:30pm")
        todoDao.insert(todo)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList[0].text).isEqualTo(todo.text)
        assertThat(todosList[0].time).isEqualTo(todo.time)
        assertThat(todosList[0].complete).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun getAllTodos_DbHasElements_AllElementsReturned() = runBlocking {
        val todo1 = Todo(id = IDGenerator().generateTodoId(), text = "wash dishes", time = "12:30pm")
        todoDao.insert(todo1)
        val todo2 = Todo(id = IDGenerator().generateTodoId(), text = "clean floors", time = "13:00pm")
        todoDao.insert(todo2)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList[0].text).isEqualTo(todo1.text)
        assertThat(todosList[1].text).isEqualTo(todo2.text)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords_EmptyDb_EmptyListReturned() = runBlocking {
        val todosList = todoDao.selectAll().first()
        assertThat(todosList).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneTodo_ValidIdProvided_TodoRemovedFromDb() = runBlocking {
        val todo1 = Todo(id = IDGenerator().generateTodoId(), text ="wash dishes", time = "12:30pm")
        todoDao.insert(todo1)

        val todo2 = Todo(id = IDGenerator().generateTodoId(), text = "clean floors", time = "13:00pm")
        todoDao.insert(todo2)

        todoDao.delete(todo1.id)

        val todosList = todoDao.selectAll().first()
        assertThat(todosList).hasSize(1)
        assertThat(todosList[0].id).isEqualTo(todo2.id)
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneTodo_NonExistentIdProvided_NoChangeInDb() = runBlocking {
        val todo1 = Todo(id = IDGenerator().generateTodoId(), text = "wash dishes", time = "12:30pm")
        todoDao.insert(todo1)

        val todo2 = Todo(id = IDGenerator().generateTodoId(), text = "clean floors", time = "13:00pm")
        todoDao.insert(todo2)

        todoDao.delete("random")

        val todosList = todoDao.selectAll().first()
        assertThat(todosList).hasSize(2)
        assertThat(todosList[0].id).isEqualTo(todo1.id)
        assertThat(todosList[1].id).isEqualTo(todo2.id)
    }

    @Test
    @Throws(Exception::class)
    fun updateTodo_ValidIdProvided_TodoUpdatedInDb() = runBlocking {
        var todo = Todo(id = IDGenerator().generateTodoId(), text = "wash dishes", time = "12:30pm")
        todoDao.insert(todo)

        todo = todoDao.selectAll().first()[0]
        todo = Todo("updated", "12:45pm", complete = true, id = todo.id)
        todoDao.insert(todo)

        val updatedTodo = todoDao.selectAll().first()[0]
        assertThat(updatedTodo.complete).isTrue()
        assertThat(updatedTodo.text).isEqualTo("updated")
        assertThat(updatedTodo.time).isEqualTo(todo.time)
    }

    @Test
    @Throws(Exception::class)
    fun updateTodo_NonExistentIdProvided_InsertInDb() = runBlocking {
        var todo = Todo(id = IDGenerator().generateTodoId(), text = "wash dishes", time = "12:30pm")
        todoDao.insert(todo)

        var nonExistentTodo = Todo("updated", "12:45pm", false, id = "1234")
        todoDao.insert(nonExistentTodo)

        val allTodos = todoDao.selectAll().first()
        val existingTodo = allTodos[0]
        val newTodo = allTodos[1]

        assertThat(allTodos.size).isEqualTo(2)

        assertThat(existingTodo.complete).isFalse()
        assertThat(existingTodo.text).isEqualTo(todo.text)
        assertThat(existingTodo.time).isEqualTo(todo.time)

        assertThat(newTodo.id).isEqualTo(nonExistentTodo.id)
        assertThat(newTodo.text).isEqualTo(nonExistentTodo.text)
        assertThat(newTodo.time).isEqualTo(nonExistentTodo.time)
    }
}