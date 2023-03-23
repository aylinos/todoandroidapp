package com.example.todoapp.data

import com.example.todoapp.data.model.TodoLocalStatus
import com.example.todoapp.data.room.TodoLocalStatusActions
import com.example.todoapp.data.room.TodoLocalStatusDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class TodoLocalStatusActionsImpl (private val dao: TodoLocalStatusDao) : TodoLocalStatusActions {
    override fun selectAll(): Flow<List<TodoLocalStatus>> {
        Dispatchers.IO.apply {
            return dao.selectAll()
        }
    }

    override suspend fun select(): List<TodoLocalStatus> {
        Dispatchers.IO.apply {
            return dao.select()
        }
    }

    override suspend fun getAll(): List<TodoLocalStatus?> {
        Dispatchers.IO.apply {
            return dao.getAll()
        }
    }

    override suspend fun getById(id: Long): TodoLocalStatus? {
        Dispatchers.IO.apply {
            return dao.getById(id)
        }
    }

    override suspend fun getByReference(reference: String): List<TodoLocalStatus> {
        Dispatchers.IO.apply {
            return dao.getByReference(reference)
        }
    }

    override suspend fun insert(element: TodoLocalStatus) {
        Dispatchers.IO.apply {
            return dao.insert(element)
        }
    }

    override suspend fun insertMany(elements: List<TodoLocalStatus>) {
        Dispatchers.IO.apply {
            return dao.insertMany(elements)
        }
    }

    override suspend fun update(element: TodoLocalStatus) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long) {
        Dispatchers.IO.apply {
            return dao.delete(id)
        }
    }

    override suspend fun deleteAll() {
        Dispatchers.IO.apply {
            return dao.deleteAll()
        }
    }
}