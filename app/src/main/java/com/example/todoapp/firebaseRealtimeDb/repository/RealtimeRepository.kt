package com.example.todoapp.firebaseRealtimeDb.repository

import com.example.todoapp.firebaseRealtimeDb.RealtimeModelResponse
import com.example.todoapp.firebaseRealtimeDb.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {
    fun insert(
        item: RealtimeModelResponse.RealtimeItems
    ) : Flow<ResultState<String>>

//    fun getItems() : Flow<ResultState<List<RealtimeModelResponse>>>
//
//    fun delete(
//        key:String
//    ) : Flow<ResultState<String>>
//
//    fun update(
//        res:RealtimeModelResponse
//    ) : Flow<ResultState<String>>
}