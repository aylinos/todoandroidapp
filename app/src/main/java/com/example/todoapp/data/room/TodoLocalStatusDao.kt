package com.example.todoapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapp.data.model.TodoLocalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoLocalStatusDao {
    @Query("SELECT * FROM todo_local_status")
    fun selectAll(): Flow<List<TodoLocalStatus>>

    @Query("SELECT * FROM todo_local_status")
    suspend fun select(): List<TodoLocalStatus>

    @Query("SELECT * from todo_local_status")
    suspend fun getAll() : List<TodoLocalStatus?>

    @Query("SELECT * from todo_local_status WHERE :id = id")
    suspend fun getById(id: Long): TodoLocalStatus?

    @Query("SELECT * from todo_local_status WHERE :reference = reference")
    suspend fun getByReference(reference: String) : List<TodoLocalStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(element: TodoLocalStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(elements: List<TodoLocalStatus>)

    @Query("DELETE From todo_local_status WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM todo_local_status")
    suspend fun deleteAll()
}