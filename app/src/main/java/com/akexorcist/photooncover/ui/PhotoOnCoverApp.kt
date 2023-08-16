package com.akexorcist.photooncover.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.akexorcist.photooncover.ui.feature.home.navigation.homeScreen


@Composable
fun PhotoOnCoverApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        homeScreen(navController = navController)
    }
}
