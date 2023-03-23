package com.example.todoapp.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.todoapp.data.model.Todo
import com.example.todoapp.ui.home.components.TodoItem

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun HomeScreen(onNavigate: (Todo?) -> Unit) {
    val viewModel = viewModel(HomeViewModel::class.java)
    val state by viewModel.state.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { onNavigate(null) },
            modifier = Modifier.testTag("plusButtonFirebaseTag")) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding) // padding applied here
        ) {
            LazyColumn(modifier = Modifier.testTag("todosListFirebaseTag")) {
                items(state.todoList) { todo ->
                    if (todo != null) {
                        TodoItem(
                            todo = todo,
                            onChecked = { viewModel.updateTodo(todo, it) },
                            onDelete = { viewModel.deleteTodo(todo) },
                            onNavigation = { onNavigate(it) },
                        )
                    }
                }
            }

        }
    }
}