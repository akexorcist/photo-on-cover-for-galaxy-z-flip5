package com.akexorcist.photooncover.core.di

import androidx.room.Room
import com.akexorcist.photooncover.core.data.DefaultPhotoRepository
import com.akexorcist.photooncover.core.data.PhotoRepository
import com.akexorcist.photooncover.core.data.db.PhotoDatabase
import com.akexorcist.photooncover.core.data.db.PhotoLocalDataSource
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
