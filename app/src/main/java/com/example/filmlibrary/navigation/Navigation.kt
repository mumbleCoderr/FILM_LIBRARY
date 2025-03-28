package com.example.filmlibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.filmlibrary.ui.screen.AddProductionScreen
import com.example.filmlibrary.ui.screen.HomeScreen
import com.example.filmlibrary.ui.screen.ProductionDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.ProductionDetailsScreen.route + "?productionId={productionId}",
            arguments = listOf(
                navArgument("productionId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { entry ->
            ProductionDetailsScreen(productionId = entry.arguments?.getString("productionId"))
        }
        composable(
            route = Screen.AddProductionScreen.route,
        ) {
            AddProductionScreen()
        }
    }
}