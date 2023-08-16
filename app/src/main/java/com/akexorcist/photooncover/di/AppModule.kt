package com.akexorcist.photooncover.di

import androidx.lifecycle.SavedStateHandle
import com.akexorcist.photooncover.core.navigation.WidgetNavigation
import com.akexorcist.photooncover.navigation.DefaultWidgetNavigation
import com.akexorcist.photooncover.ui.feature.home.HomeViewModel
import com.akexorcist.photooncover.widget.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val modules = module {
        viewModel { (savedStateHandle: SavedStateHandle) -> HomeViewModel(savedStateHandle, get()) }
        viewModel { PhotoViewModel(get()) }

        factory<WidgetNavigation> { DefaultWidgetNavigation() }
    }
}
