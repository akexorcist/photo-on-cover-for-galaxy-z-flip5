package com.akexorcist.photooncover.di

import com.akexorcist.photooncover.ui.feature.home.HomeViewModel
import com.akexorcist.photooncover.widget.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val modules = module {
        viewModel { HomeViewModel(get()) }
        viewModel { PhotoViewModel() }
    }
}
