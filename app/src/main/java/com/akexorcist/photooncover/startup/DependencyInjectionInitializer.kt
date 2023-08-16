package com.akexorcist.photooncover.startup

import android.content.Context
import androidx.startup.Initializer
import com.akexorcist.photooncover.base.core.di.CoreModule
import com.akexorcist.photooncover.di.AppModule
import com.akexorcist.photooncover.feature.home.di.HomeModule
import com.akexorcist.photooncover.feature.widget.di.WidgetModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class DependencyInjectionInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        startKoin {
            androidContext(context.applicationContext)
            modules(AppModule.modules)
            modules(CoreModule.modules)
            modules(HomeModule.modules)
            modules(WidgetModule.modules)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}
