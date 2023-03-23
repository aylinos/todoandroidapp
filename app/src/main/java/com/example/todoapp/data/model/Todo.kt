package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = "todo")
data class Todo(
    val text: String = "",
    val time: String? = "",
    val complete: Boolean = false,
    @PrimaryKey
    val id: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "time" to time,
            "complete" to complete
        )
    }

    fun validator() {
        if (text.isBlank()) {
            throw IllegalArgumentException("Text should not be blank")
        }
    }
}
