package com.akexorcist.photooncover.feature.instruction.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun rememberInstructionScreenNavigator(
    navController: NavController,
): InstructionScreenNavigator {
    return koinInject {
        parametersOf(navController)
    }
}

interface InstructionScreenNavigator {
    fun navigateToHome()
}
