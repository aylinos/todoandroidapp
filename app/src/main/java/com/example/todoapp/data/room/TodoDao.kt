package com.example.todoapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapp.data.Todo
import kotlinx.coroutines.flow.Flow
import java.sql.SQLException

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun selectAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Query("DELETE From todo WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM todo")
    suspend fun deleteAllTodo()

    @Throws(SQLException::class)
    @Query("UPDATE todo SET isComplete = :isComplete WHERE id = :id")
    suspend fun updateComplTodo(isComplete: Boolean, id: Long)
}