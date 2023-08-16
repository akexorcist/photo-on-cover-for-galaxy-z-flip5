package com.akexorcist.photooncover.feature.viewer.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.akexorcist.photooncover.feature.viewer.ViewerRoute

@Suppress("ConstPropertyName")
private const val FileNameParam = "fileName"

const val ViewerNavigationRoute = "viewer?$FileNameParam={$FileNameParam}"

fun NavController.navigateToViewer(fileName: String, navOptions: NavOptions? = null) {
    this.navigate(
        route = ViewerNavigationRoute.replace("{$FileNameParam}", fileName),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.viewerScreen(navController: NavController) {
    composable(
        route = ViewerNavigationRoute,
        enterTransition = { ->
            fadeIn() + slideInVertically(
                initialOffsetY = { fullHeight -> (fullHeight * 0.02).toInt() }
            )
        },
        exitTransition = {
            fadeOut() + slideOutVertically(
                targetOffsetY = { fullHeight -> (fullHeight * 0.02).toInt() }
            )
        },
        arguments = listOf(
            navArgument(FileNameParam) {
                nullable = true
                defaultValue = null
            },
        )
    ) { backStackEntry ->
        val fileName = backStackEntry.arguments?.getString(FileNameParam)
        ViewerRoute(
            navController = navController,
            fileName = fileName,
        )
    }
}
