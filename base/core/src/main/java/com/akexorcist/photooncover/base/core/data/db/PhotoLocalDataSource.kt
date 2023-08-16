package com.akexorcist.photooncover.base.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoLocalDataSource {
    @Query("SELECT * FROM photo ORDER BY `order` ASC")
    fun getAllPhotosAsFlow(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo ORDER BY `order` ASC")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Query("SELECT COUNT(*) FROM photo")
    suspend fun getPhotoCount(): Int

    @Insert
    suspend fun insert(photo: PhotoEntity)

    @Delete
    suspend fun delete(photo: PhotoEntity)

    @Query("SELECT MAX(`order`) FROM photo")
    suspend fun getMaxOrder(): Int

    @Query("SELECT * FROM photo WHERE `order` = :order")
    suspend fun getPhoto(order: Int): PhotoEntity?

    @Query("UPDATE photo SET `order` = :newOrder WHERE `id` = :id")
    suspend fun updatePhotoOrder(id: String?, newOrder: Int)

    @Query("UPDATE photo SET `order` = `order` + 1 WHERE `order` >= :startOrder AND `order` <= :endOrder")
    suspend fun shiftPhotoOrderUp(startOrder: Int, endOrder: Int)

    @Query("UPDATE photo SET `order` = `order` - 1 WHERE `order` >= :startOrder AND `order` <= :endOrder")
    suspend fun shiftPhotoOrderDown(startOrder: Int, endOrder: Int)

    @Transaction
    suspend fun updatePhotoOrderAndShiftUp(id: String, newOrder: Int, startOrder: Int, endOrder: Int) {
        shiftPhotoOrderUp(startOrder, endOrder)
        updatePhotoOrder(id, newOrder)
    }

    @Transaction
    suspend fun updatePhotoOrderAndShiftDown(id: String, newOrder: Int, startOrder: Int, endOrder: Int) {
        shiftPhotoOrderDown(startOrder, endOrder)
        updatePhotoOrder(id, newOrder)
    }

    @Query("DELETE FROM photo WHERE id IN (:ids)")
    suspend fun deletePhotosByIds(ids: List<String>)

    @Transaction
    suspend fun deleteAndReindexPhotos(ids: List<String>) {
        deletePhotosByIds(ids)
        val photos: List<PhotoEntity> = getAllPhotos()
        photos.forEachIndexed { index, photo ->
            updatePhotoOrder(photo.id, index)
        }
    }

    @Transaction
    suspend fun getLatestOrder(): Int? {
        return getMaxOrder().takeIf { it != 0 }
            ?: getPhotoCount().takeIf { it == 1 }?.let { 0 }
    }
}
