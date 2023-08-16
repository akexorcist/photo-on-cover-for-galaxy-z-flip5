package com.akexorcist.photooncover.base.core.data

import android.content.Context
import android.net.Uri
import com.akexorcist.photooncover.base.core.data.db.PhotoEntity
import com.akexorcist.photooncover.base.core.data.db.PhotoLocalDataSource
import com.akexorcist.photooncover.base.core.utility.FileUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

interface PhotoRepository {
    fun getPhotos(): Flow<List<PhotoData>>

    suspend fun addNewPhoto(uri: Uri, extension: String): Boolean

    suspend fun changePhotoOrder(fromOrder: Int, toOrder: Int)

    suspend fun deletePhotos(photos: List<PhotoData>)
}

class DefaultPhotoRepository(
    private val context: Context,
    private val dataSource: PhotoLocalDataSource,
) : PhotoRepository {
    override fun getPhotos(): Flow<List<PhotoData>> {
        return dataSource.getAllPhotosAsFlow().map { items ->
            items.map { entity ->
                entity.toData()
            }
        }
    }

    override suspend fun addNewPhoto(uri: Uri, extension: String): Boolean {
        val fileName = UUID.randomUUID().toString()
        val order = dataSource.getLastOrder() + 1
        copyPhotoToInternalStorage(
            context = context,
            fileName = fileName,
            extension = extension,
            uri = uri,
        ) ?: return false
        val photo = PhotoEntity(
            id = fileName,
            path = fileName + extension,
            order = order,
        )
        dataSource.insert(photo = photo)
        return true
    }

    override suspend fun changePhotoOrder(fromOrder: Int, toOrder: Int) {
        dataSource.getPhoto(order = fromOrder)?.let { photo ->
            val currentOrder: Int = photo.order
            when {
                currentOrder > toOrder ->
                    dataSource.updatePhotoOrderAndShiftUp(
                        id = photo.id,
                        newOrder = toOrder,
                        startOrder = toOrder,
                        endOrder = currentOrder - 1,
                    )

                currentOrder < toOrder ->
                    dataSource.updatePhotoOrderAndShiftDown(
                        id = photo.id,
                        newOrder = toOrder,
                        startOrder = currentOrder + 1,
                        endOrder = toOrder,
                    )
            }
        }
    }

    override suspend fun deletePhotos(photos: List<PhotoData>) {
        dataSource.deleteAndReindexPhotos(photos.map { it.id })
        photos.forEach { photo ->
            deletePhotoInInternalStorage(context, photo.fileName)
        }
    }

    private fun deletePhotoInInternalStorage(context: Context, fileNameWithExtension: String) {
        val directory = FileUtility.getPhotoDirectory(context)
        val file = File(directory, fileNameWithExtension)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun copyPhotoToInternalStorage(context: Context, fileName: String, extension: String, uri: Uri): Uri? {
        val directory = FileUtility.getPhotoDirectory(context)
        val file = File(directory, fileName + extension)
        val outputStream = FileOutputStream(file)
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        inputStream.use {
            outputStream.use {
                inputStream.copyTo(outputStream)
            }
        }
        return Uri.fromFile(file)
    }
}
