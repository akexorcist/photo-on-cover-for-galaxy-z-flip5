package com.akexorcist.photooncover.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.WidgetScreenNavigator
import com.akexorcist.photooncover.navigation.DefaultWidgetScreenNavigator
import com.akexorcist.photooncover.feature.home.HomeViewModel
import com.akexorcist.photooncover.navigation.DefaultHomeScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator
import com.akexorcist.photooncover.feature.instruction.navigation.InstructionScreenNavigator
import com.akexorcist.photooncover.feature.widget.PhotoViewModel
import com.akexorcist.photooncover.navigation.DefaultInstructionScreenNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val modules = module {
        viewModel { (savedStateHandle: SavedStateHandle) -> HomeViewModel(savedStateHandle, get()) }
        viewModel { PhotoViewModel(get()) }

        factory<WidgetScreenNavigator> { DefaultWidgetScreenNavigator() }

        factory<HomeScreenNavigator> { (navController: NavController) ->
            DefaultHomeScreenNavigator(navController)
        }
        factory<InstructionScreenNavigator> { (navController: NavController) ->
            DefaultInstructionScreenNavigator(navController)
        }
    }
}
