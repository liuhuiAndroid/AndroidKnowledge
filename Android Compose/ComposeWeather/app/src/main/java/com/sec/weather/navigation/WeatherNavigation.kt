package com.sec.weather.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sec.weather.screens.about.AboutScreen
import com.sec.weather.screens.favorites.FavoritesScreen
import com.sec.weather.screens.main.MainScreen
import com.sec.weather.screens.main.MainViewModel
import com.sec.weather.screens.search.SearchScreen
import com.sec.weather.screens.settings.SettingsScreen
import com.sec.weather.screens.splash.SplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WeatherScreens.SplashScreen.name
    ) {
        composable(WeatherScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(
            "${WeatherScreens.MainScreen.name}/{cityId}/{cityName}",
            arguments = listOf(
                navArgument(name = "cityId") {
                    type = NavType.StringType
                },
                navArgument(name = "cityName") {
                    type = NavType.StringType
                },
            )
        ) { navBack ->
            val cityId = navBack.arguments?.getString("cityId")
            val cityName = navBack.arguments?.getString("cityName")
            if (!cityId.isNullOrBlank() && !cityName.isNullOrBlank()) {
                val mainViewModel = hiltViewModel<MainViewModel>()
                MainScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    cityId = cityId,
                    cityName = cityName
                )
            }
        }
        composable(WeatherScreens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }
        composable(WeatherScreens.SettingsScreen.name) {
            SettingsScreen(navController = navController)
        }
        composable(WeatherScreens.FavoriteScreen.name) {
            FavoritesScreen(navController = navController)
        }
    }
}