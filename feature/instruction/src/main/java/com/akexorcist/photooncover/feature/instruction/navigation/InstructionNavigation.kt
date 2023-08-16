package com.akexorcist.photooncover.feature.instruction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.akexorcist.photooncover.feature.instruction.InstructionRoute
import com.akexorcist.photooncover.feature.instruction.R

const val InstructionNavigationRoute = "instruction"

fun NavController.navigateToInstruction(navOptions: NavOptions? = null) {
    this.navigate(
        route = InstructionNavigationRoute,
        navOptions = navOptions ?: NavOptions.Builder().apply {
            setEnterAnim(R.anim.instruction_enter_animation)
            setPopEnterAnim(R.anim.instruction_enter_animation)
            setExitAnim(R.anim.instruction_exit_animation)
            setPopExitAnim(R.anim.instruction_exit_animation)
        }.build()
    )
}

fun NavGraphBuilder.instructionScreen(navController: NavController) {
    composable(
        route = InstructionNavigationRoute
    ) {
        InstructionRoute(
            navController = navController,
        )
    }
}
