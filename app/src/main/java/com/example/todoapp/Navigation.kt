package com.example.todoapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.data.model.Todo
import com.example.todoapp.ui.detail.DetailScreen
import com.example.todoapp.ui.home.HomeScreen

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home_route")
    object Detail : NavRoute("detail_route")
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun TodoNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
    ) {
        composable(NavRoute.Home.route) {
            HomeScreen { todo: Todo? ->
                navController.navigate(NavRoute.Detail.route + "/${todo?.id ?: "-1"}") {

                }
            }
        }
        composable(
            NavRoute.Detail.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) {
            DetailScreen(selectedId = it.arguments?.getString("id") ?: "-1") {
                navController.navigateUp()
            }
        }

    }
}