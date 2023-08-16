package com.akexorcist.photooncover.feature.home.di

import androidx.lifecycle.SavedStateHandle
import com.akexorcist.photooncover.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    val modules = module {
        viewModel { (savedStateHandle: SavedStateHandle) -> HomeViewModel(savedStateHandle, get()) }
    }
}
