package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.util.StatusType

@Entity(tableName = "todo_local_status")
data class TodoLocalStatus(
    val reference: String,
    val status: StatusType,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {}
