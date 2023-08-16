package com.akexorcist.photooncover.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.ScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator
import com.akexorcist.photooncover.feature.instruction.navigation.navigateToInstruction

class DefaultHomeScreenNavigator(
    private val navController: NavController
): ScreenNavigator, HomeScreenNavigator {
    override fun navigateToViewer() {
        // TODO navigate to viewer
    }

    override fun navigateToInstruction() {
        navController.navigateToInstruction()
    }
}
