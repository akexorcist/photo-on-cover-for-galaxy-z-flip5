package com.akexorcist.photooncover.navigation

import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.ScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.navigateToHome
import com.akexorcist.photooncover.feature.instruction.navigation.InstructionScreenNavigator

class DefaultInstructionScreenNavigator(
    private val navController: NavController
): ScreenNavigator, InstructionScreenNavigator {
    override fun navigateToHome() {
        navController.navigateToHome()
    }
}
