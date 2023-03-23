package com.example.todoapp.data.room

import androidx.room.*
import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun selectAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Query("DELETE From todo WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * from todo WHERE :id = id")
    suspend fun getById(id: String): Todo?
}