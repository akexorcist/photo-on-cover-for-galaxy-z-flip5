package com.akexorcist.photooncover.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoLocalDataSource {
    @Query("SELECT * FROM photo")
    fun getAll(): Flow<List<PhotoEntity>>

    @Insert
    suspend fun insert(photo: PhotoEntity)

    @Delete
    suspend fun delete(photo: PhotoEntity)

    @Query("SELECT MAX(`order`) FROM photo")
    suspend fun getLastOrder(): Int
}
