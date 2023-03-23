package com.example.todoapp.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.todoapp.Graph
import com.example.todoapp.util.NetworkConnectionManager
import com.example.todoapp.util.ServiceHandler
import kotlinx.coroutines.*

class NetworkStatusChangeReceiver(
    private val serviceHandler: ServiceHandler = Graph.serviceHandler
) : BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob())
    private fun syncTodosAsync(): Deferred<Unit> = scope.async { serviceHandler.syncTodos() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        val isConnected = NetworkConnectionManager.isOnline()

        if(isConnected) {
            syncTodosAsync()
        }
    }
}