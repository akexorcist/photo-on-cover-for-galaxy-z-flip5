package com.akexorcist.photooncover.ui.feature.home.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.core.navigation.ScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator

class DefaultHomeScreenNavigator(
    navController: NavController
): ScreenNavigator, HomeScreenNavigator {
    override fun navigateToViewer() {
        // TODO navigate to viewer
    }
}