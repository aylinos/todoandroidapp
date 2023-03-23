package com.example.todoapp

import android.app.Application
import com.example.todoapp.util.NetworkConnectionManager

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.initialize(this)
        NetworkConnectionManager.initialize(this)
    }
}