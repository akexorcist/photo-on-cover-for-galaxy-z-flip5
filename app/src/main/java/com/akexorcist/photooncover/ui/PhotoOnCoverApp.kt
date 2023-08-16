package com.akexorcist.photooncover.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.akexorcist.photooncover.feature.home.navigation.HomeNavigationRoute
import com.akexorcist.photooncover.feature.home.navigation.homeScreen
import com.akexorcist.photooncover.feature.instruction.navigation.instructionScreen
import com.akexorcist.photooncover.feature.viewer.navigation.viewerScreen


@Composable
fun PhotoOnCoverApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeNavigationRoute,
    ) {
        homeScreen(navController = navController)
        instructionScreen(navController = navController)
        viewerScreen(navController = navController)
    }
}
