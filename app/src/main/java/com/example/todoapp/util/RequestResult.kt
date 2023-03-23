package com.example.todoapp.util

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error<out T>(val exception: Throwable, val data: T) : RequestResult<T>()
//    object Loading : FirebaseResult<Nothing>()
    class Loading<T> (val data: T? = null): RequestResult<Nothing>()
}