package com.akexorcist.photooncover.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.ScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.navigateToHome
import com.akexorcist.photooncover.feature.viewer.navigation.ViewerScreenNavigator

class DefaultViewerScreenNavigation(
    private val navController: NavController
) : ScreenNavigator, ViewerScreenNavigator {
    override fun navigateToHome() {
        navController.navigateToHome()
    }
}
