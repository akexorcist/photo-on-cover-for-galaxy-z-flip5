package com.akexorcist.photooncover.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.akexorcist.photooncover.feature.home.HomeRoute

const val HomeNavigationRoute = "home"

fun NavController.navigateToHome() {
    this.navigate(
        route = HomeNavigationRoute,
        navOptions = navOptions {
            popUpTo(HomeNavigationRoute) {
                inclusive = true
            }
        },
    )
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(
        route = HomeNavigationRoute
    ) {
        HomeRoute(
            navController = navController,
        )
    }
}