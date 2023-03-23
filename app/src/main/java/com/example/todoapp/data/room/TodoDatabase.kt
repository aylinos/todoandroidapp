package com.example.todoapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.model.TodoLocalStatus

@Database(entities = [Todo::class, TodoLocalStatus::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    abstract fun todoLocalStatusDao(): TodoLocalStatusDao

    //Instantiate & Access the database
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db" //db name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}