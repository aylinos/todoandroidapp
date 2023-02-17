package com.example.todoapp

import android.app.Application

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.initialize(this)
    }
}