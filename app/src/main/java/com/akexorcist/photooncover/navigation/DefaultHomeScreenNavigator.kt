package com.akexorcist.photooncover.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.ScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator
import com.akexorcist.photooncover.feature.instruction.navigation.navigateToInstruction
import com.akexorcist.photooncover.feature.viewer.navigation.navigateToViewer

class DefaultHomeScreenNavigator(
    private val navController: NavController
) : ScreenNavigator, HomeScreenNavigator {
    override fun navigateToViewer(fileName: String) {
        navController.navigateToViewer(
            fileName = fileName
        )
    }

    override fun navigateToInstruction() {
        navController.navigateToInstruction()
    }
}
