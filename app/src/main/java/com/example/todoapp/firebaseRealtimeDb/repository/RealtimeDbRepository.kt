package com.example.todoapp.firebaseRealtimeDb.repository

import com.example.todoapp.firebaseRealtimeDb.RealtimeModelResponse
import com.example.todoapp.firebaseRealtimeDb.utils.ResultState
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDbRepository @Inject constructor(
    private val db: DatabaseReference
) : RealtimeRepository {
    override fun insert(item: RealtimeModelResponse.RealtimeItems): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)

        db.push().setValue(
            item
        ).addOnCompleteListener {
            if(it.isSuccessful)
                trySend(ResultState.Success("Data inserted Successfully.."))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }

        awaitClose {
            close()
        }
    }
}

