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
    fun getAllWords_DbHasElements_AllElementsReturned() = runBlocking {
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
    fun getAllWords_EmptyDb_EmptyListReturned() = runBlocking {
        val todosList = todoDao.selectAll().first()
        assertThat(todosList).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun deleteOneTodo_ValidIdProvided_TodoRemovedFromDb() = runBlocking {
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
    fun deleteOneTodo_NonExistentIdProvided_NoChangeInDb() = runBlocking {
        val todo1 = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo1)
        val todo2 = Todo("clean floors", "13:00pm")
        todoDao.insert(todo2)
        todoDao.delete(3)
        val todosList = todoDao.selectAll().first()
        assertThat(todosList).hasSize(2)
        assertThat(todosList[0].id).isEqualTo(1)
        assertThat(todosList[1].id).isEqualTo(2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll_DbHasElements_EmptiedDb() = runBlocking {
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
    fun deleteAll_EmptyDb_NoChangeInDb() = runBlocking {
        val todosListBefore = todoDao.selectAll().first()
        todoDao.deleteAllTodo()
        val todosListAfter = todoDao.selectAll().first()
        assertThat(todosListBefore).isEmpty()
        assertThat(todosListAfter).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun updateComplTodo_ValidIdProvided_TodoComplUpdatedInDb() = runBlocking {
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
    fun updateComplTodo_NonExistentIdProvided_NoChangeInDb() = runBlocking {
        var todo = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo)
        todo = todoDao.selectAll().first()[0]
        todoDao.updateComplTodo(true,2)
        val allTodos = todoDao.selectAll().first()
        val updatedTodo = allTodos[0]
        assertThat(allTodos.size).isEqualTo(1)
        assertThat(updatedTodo.isComplete).isFalse()
        assertThat(updatedTodo.todo).isEqualTo(todo.todo)
        assertThat(updatedTodo.time).isEqualTo(todo.time)
    }

    @Test
    @Throws(Exception::class)
    fun updateTodo_ValidIdProvided_TodoUpdatedInDb() = runBlocking {
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

    @Test
    @Throws(Exception::class)
    fun updateTodo_NonExistentIdProvided_InsertInDb() = runBlocking {
        var todo = Todo("wash dishes", "12:30pm")
        todoDao.insert(todo)
        var nonExistentTodo = Todo("updated", "12:45pm", false, id = 5)
        todoDao.insert(nonExistentTodo)
        val allTodos = todoDao.selectAll().first()
        val existingTodo = allTodos[0]
        val newTodo = allTodos[1]
        assertThat(allTodos.size).isEqualTo(2)
        assertThat(existingTodo.isComplete).isFalse()
        assertThat(existingTodo.todo).isEqualTo(todo.todo)
        assertThat(existingTodo.time).isEqualTo(todo.time)

        assertThat(newTodo.id).isEqualTo(nonExistentTodo.id)
        assertThat(newTodo.todo).isEqualTo(nonExistentTodo.todo)
        assertThat(newTodo.time).isEqualTo(nonExistentTodo.time)
    }
}