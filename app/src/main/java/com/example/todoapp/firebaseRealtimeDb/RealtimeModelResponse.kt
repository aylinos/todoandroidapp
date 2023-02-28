package com.example.todoapp.firebaseRealtimeDb

data class RealtimeModelResponse(val item:RealtimeItems?,
                                 val key:String? = "") {
    data class RealtimeItems(
        val text:String? = ""
    )
}
