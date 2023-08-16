package com.akexorcist.photooncover.di

import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.WidgetScreenNavigator
import com.akexorcist.photooncover.navigation.DefaultWidgetScreenNavigator
import com.akexorcist.photooncover.navigation.DefaultHomeScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator
import com.akexorcist.photooncover.feature.instruction.navigation.InstructionScreenNavigator
import com.akexorcist.photooncover.navigation.DefaultInstructionScreenNavigator
import org.koin.dsl.module

object AppModule {
    val modules = module {
        factory<WidgetScreenNavigator> { DefaultWidgetScreenNavigator() }

        factory<HomeScreenNavigator> { (navController: NavController) ->
            DefaultHomeScreenNavigator(navController)
        }
        factory<InstructionScreenNavigator> { (navController: NavController) ->
            DefaultInstructionScreenNavigator(navController)
        }
    }
}
