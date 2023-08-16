package com.akexorcist.photooncover.feature.viewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun rememberViewerScreenNavigator(
    navController: NavController,
): ViewerScreenNavigator {
    return koinInject {
        parametersOf(navController)
    }
}

interface ViewerScreenNavigator {
    fun navigateToHome()
}
