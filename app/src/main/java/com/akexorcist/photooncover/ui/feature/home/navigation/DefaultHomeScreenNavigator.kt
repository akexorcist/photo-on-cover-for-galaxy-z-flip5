package com.akexorcist.photooncover.ui.feature.home.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.navigation.ScreenNavigator

class DefaultHomeScreenNavigator(
    navController: NavController
): ScreenNavigator, HomeScreenNavigator{
    override fun navigateToViewer() {
        // TODO navigate to viewer
    }
}