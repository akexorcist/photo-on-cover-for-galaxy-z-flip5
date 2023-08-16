package com.akexorcist.photooncover.feature.widget.di

import com.akexorcist.photooncover.feature.widget.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object WidgetModule {
    val modules = module {
        viewModel { PhotoViewModel(get()) }
    }
}
