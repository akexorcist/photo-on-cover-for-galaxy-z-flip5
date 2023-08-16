package com.akexorcist.photooncover.feature.instruction.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akexorcist.photooncover.feature.instruction.InstructionRoute

const val InstructionNavigationRoute = "instruction"

fun NavController.navigateToInstruction(navOptions: NavOptions? = null) {
    this.navigate(
        route = InstructionNavigationRoute,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.instructionScreen(navController: NavController) {
    composable(
        route = InstructionNavigationRoute,
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
    ) {
        InstructionRoute(
            navController = navController,
        )
    }
}
