package com.example.todoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.data.Todo
import com.example.todoapp.ui.detail.DetailScreen
import com.example.todoapp.ui.home.HomeScreen
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home_route")
    object Detail : NavRoute("detail_route")
}

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()
    val rootRef = FirebaseDatabase.getInstance("https://todo-app-f7567-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("")
//    val todosRef = rootRef

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
    ) {
        composable(NavRoute.Home.route) {
            HomeScreen { todo: Todo? ->
                navController.navigate(NavRoute.Detail.route + "/${todo?.id ?: -1}") {

                }
            }
        }
        composable(
            NavRoute.Detail.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
        ) {
            DetailScreen(selectedId = it.arguments?.getLong("id") ?: -1, rootRef) {
                navController.navigateUp()
            }
        }

    }
}