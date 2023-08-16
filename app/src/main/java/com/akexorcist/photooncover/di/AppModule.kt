package com.akexorcist.photooncover.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.navigation.WidgetNavigation
import com.akexorcist.photooncover.navigation.DefaultWidgetNavigation
import com.akexorcist.photooncover.feature.home.HomeViewModel
import com.akexorcist.photooncover.ui.feature.home.navigation.DefaultHomeScreenNavigator
import com.akexorcist.photooncover.feature.home.navigation.HomeScreenNavigator
import com.akexorcist.photooncover.widget.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val modules = module {
        viewModel { (savedStateHandle: SavedStateHandle) -> HomeViewModel(savedStateHandle, get()) }
        viewModel { PhotoViewModel(get()) }

        factory<WidgetNavigation> { DefaultWidgetNavigation() }

        factory<HomeScreenNavigator> { (navController: NavController) ->
            DefaultHomeScreenNavigator(navController)
        }
    }
}
