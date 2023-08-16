package com.akexorcist.photooncover.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun rememberHomeScreenNavigator(
    navController: NavController,
): HomeScreenNavigator {
    return koinInject {
        parametersOf(navController)
    }
}

interface HomeScreenNavigator {
    fun navigateToViewer()
}
