package com.example.todoapp.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.Todo
import com.example.todoapp.ui.home.components.TodoItem

@Composable
fun HomeScreen(onNavigate: (Todo?) -> Unit) {
    val viewModel = viewModel(HomeViewModel::class.java)
    val state by viewModel.state.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { onNavigate(null) },
            modifier = Modifier.testTag("plusButtonTag")) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) { innerPadding -> // padding calculated by scaffold
        // it doesn't have to be a column,
        // can be another component that accepts a modifier with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding) // padding applied here
        ) {
            LazyColumn(modifier = Modifier.testTag("todosListTag")) {
                items(state.todoList) { todo ->
                    Log.d("HomeScreen", "Todo name and id: ${todo.todo} ${todo.id}")
                    TodoItem(
                        todo = todo,
                        onChecked = { viewModel.updateTodo(it, todo.id) },
                        onDelete = { viewModel.delete(it) },
                        onNavigation = { onNavigate(it) },
                    )
                }
            }

        }
    }


}