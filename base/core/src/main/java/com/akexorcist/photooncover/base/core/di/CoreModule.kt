package com.akexorcist.photooncover.base.core.di

import androidx.room.Room
import com.akexorcist.photooncover.base.core.data.DefaultPhotoRepository
import com.akexorcist.photooncover.base.core.data.PhotoRepository
import com.akexorcist.photooncover.base.core.data.db.PhotoDatabase
import com.akexorcist.photooncover.base.core.data.db.PhotoLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object CoreModule {
    @Suppress("RemoveExplicitTypeArguments")
    val modules = module {
        single<PhotoDatabase> {
            Room.databaseBuilder(
                androidContext(),
                PhotoDatabase::class.java, "photo.db",
            ).build()
        }
        factory<PhotoLocalDataSource> { get<PhotoDatabase>().photo() }

        single<PhotoRepository> { DefaultPhotoRepository(androidContext(), get()) }
    }
}
