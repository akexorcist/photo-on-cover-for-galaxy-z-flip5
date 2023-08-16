package com.akexorcist.photooncover.ui.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akexorcist.photooncover.ui.feature.home.HomeRoute

const val HomeNavigationRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(
        route = HomeNavigationRoute,
        navOptions = navOptions,
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