package com.example.filmlibrary.navigation

sealed class Screen(
    val route: String,
) {
    object HomeScreen: Screen("home_screen")
    object AddProductionScreen: Screen("add_production_screen")
    object ProductionDetailsScreen: Screen("production_details_screen")
}