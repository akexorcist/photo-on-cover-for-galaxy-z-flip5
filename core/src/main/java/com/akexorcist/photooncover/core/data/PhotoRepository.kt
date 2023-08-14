package com.akexorcist.photooncover.core.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.akexorcist.photooncover.core.data.db.PhotoEntity
import com.akexorcist.photooncover.core.data.db.PhotoLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

interface PhotoRepository {
    fun getPhotos(): Flow<List<PhotoData>>

    suspend fun addNewPhoto(uri: Uri): Boolean
}

class DefaultPhotoRepository(
    private val context: Context,
    private val dataSource: PhotoLocalDataSource,
) : PhotoRepository {
    override fun getPhotos(): Flow<List<PhotoData>> {
        return dataSource.getAll().map { items -> items.map { entity -> entity.toData() } }
    }

    override suspend fun addNewPhoto(originalUri: Uri): Boolean {
        val fileName = UUID.randomUUID().toString()
        val uri: Uri = copyPhotoToInternalStorage(
            context = context,
            fileName = fileName,
            uri = originalUri,
        ) ?: return false
        val photo = PhotoEntity(
            id = fileName,
            path = uri.toString(),
        )
        dataSource.insert(photo = photo)
        return true
    }

    private fun copyPhotoToInternalStorage(context: Context, fileName: String, uri: Uri): Uri? {
        val directory = File(context.filesDir, "photo")
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(directory, fileName)
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
