package com.example.todoapp

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.todoapp.util.receiver.NetworkStatusChangeReceiver
import com.example.todoapp.ui.theme.ToDoAppTheme


class MainActivity : ComponentActivity() {
    lateinit var receiver : NetworkStatusChangeReceiver

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TodoNavHost()
                }
            }
        }

        receiver = NetworkStatusChangeReceiver()

        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE").also {
            registerReceiver(receiver, it)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}

