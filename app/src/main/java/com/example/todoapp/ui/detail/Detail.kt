package com.example.todoapp.ui.detail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.model.Todo

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun DetailScreen(
    selectedId: String,
    onNavigate: () -> Unit,
) {
    Log.d("DetailScreen", "Started detail screen with id: $selectedId")
    val viewModel = viewModel(
        DetailViewModel::class.java,
        factory = DetailViewModelFactory(selectedId)
    )
    val state by viewModel.state.collectAsState()
    DetailScreenComponent(todoText = state.text,
        onTodoTextChange = { viewModel.onTextChange(it) },
        timeText = state.time,
        onTimeTextChange = { viewModel.onTimeChange(it) },
        onNavigate = { onNavigate() },
        onSaveTodo = {
            if(selectedId == "-1") viewModel.insertTodo(it)
            else viewModel.updateTodo(it)
        },
        selectedId = state.selectId)
}

@Composable
fun DetailScreenComponent(
    todoText: String,
    onTodoTextChange: (String) -> Unit,
    timeText: String,
    onTimeTextChange: (String) -> Unit,
    onNavigate: () -> Unit,
    onSaveTodo: (Todo) -> Unit,
    selectedId: String,
) {
//    val context = LocalContext.current
    val isTodoEdit = selectedId != "-1" //if id==-1 => mode create, not edit
    Log.d("DetailScreenComponent", "Is it edit screen? : $isTodoEdit")
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = todoText,
            onValueChange = { onTodoTextChange(it) },
            label = { Text(text = "Enter Todo") },
            modifier = Modifier.testTag("todoTextTag")
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = timeText,
            onValueChange = { onTimeTextChange(it) },
            label = { Text(text = "Enter Time") },
            modifier = Modifier.testTag("todoTimeTextTag")
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            val todo = if (!isTodoEdit) Todo(todoText, timeText)
            else Todo(todoText, timeText, id = selectedId)
            onSaveTodo(todo)
            onNavigate()
        },
            modifier = Modifier.testTag("createButtonTag")) {
            val text = if (isTodoEdit) "Update Todo" else "Save todo"
            Text(text = text)
        }
    }
}