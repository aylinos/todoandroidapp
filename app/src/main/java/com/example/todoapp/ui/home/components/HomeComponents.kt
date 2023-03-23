package com.example.todoapp.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Todo

//Represents a single item
@Composable
fun TodoItem(
    //parameters
    todo: Todo,
    onChecked: (Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
    onNavigation: (Todo) -> Unit
) {
    //Screen UI
    Card(backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onNavigation(todo) },
    ) {
        //row
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(checked = todo.complete, onCheckedChange = { onChecked(it) })
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                todo.text?.let { Text(text = it, style = MaterialTheme.typography.subtitle2) }
                Spacer(modifier = Modifier.size(16.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = todo.time?:"None", style = MaterialTheme.typography.body2)
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(onClick = { onDelete(todo) }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }

        }
    }
}