package com.akexorcist.photooncover.core.data

import android.content.Context
import android.net.Uri
import com.akexorcist.photooncover.core.data.db.PhotoEntity
import com.akexorcist.photooncover.core.data.db.PhotoLocalDataSource
import com.akexorcist.photooncover.core.utility.FileUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

interface PhotoRepository {
    fun getPhotos(): Flow<List<PhotoData>>

    suspend fun addNewPhoto(uri: Uri, extension: String): Boolean
}

class DefaultPhotoRepository(
    private val context: Context,
    private val dataSource: PhotoLocalDataSource,
) : PhotoRepository {
    override fun getPhotos(): Flow<List<PhotoData>> {
        return dataSource.getAll().map { items ->
            items.map { entity ->
                entity.toData()
            }
        }
    }

    override suspend fun addNewPhoto(originalUri: Uri, extension: String): Boolean {
        val fileName = UUID.randomUUID().toString()
        val order = dataSource.getLastOrder() + 1
        copyPhotoToInternalStorage(
            context = context,
            fileName = fileName,
            extension = extension,
            uri = originalUri,
        ) ?: return false
        val photo = PhotoEntity(
            id = fileName,
            path = fileName + extension,
            order = order,
        )
        dataSource.insert(photo = photo)
        return true
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
