package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.core.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val IS_DELETE_MODE_KEY = "isDeleteMode"
private const val IS_INSTRUCTION_MODE_KEY = "isInstructionMode"

class HomeViewModel(
    private val handle: SavedStateHandle,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _isDeleteMode = handle.getStateFlow(
        key = IS_DELETE_MODE_KEY,
        initialValue = false,
    )

    private val _isInstructionMode = handle.getStateFlow(
        key = IS_INSTRUCTION_MODE_KEY,
        initialValue = false
    )
    private val _markedAsDeletePhotos: MutableStateFlow<List<PhotoData>> = MutableStateFlow(listOf())

    val uiState: Flow<HomeUiState> = photoRepository.getPhotos().combine(_isInstructionMode) { photos, isInstructionMode ->
        HomeUiState.perform(
            photos = photos,
            isDeleteMode = false,
            isInstructionMode = isInstructionMode,
        )
    }.combine(_isDeleteMode) { state, isDeleteMode ->
        HomeUiState.perform(
            photos = state.photos,
            isDeleteMode = isDeleteMode,
            isInstructionMode =
            if (isDeleteMode) false
            else state is HomeUiState.InstructionMode,
        )
    }.combine(_markedAsDeletePhotos) { state, markedAsDeletePhotos ->
        state.update(
            state.photos.map { photo ->
                photo.copy(
                    markAsDelete = markedAsDeletePhotos.any { deletePhoto ->
                        deletePhoto.id == photo.id
                    }
                )
            }
        )
    }

    fun addPhoto(uri: Uri, format: Bitmap.CompressFormat) = viewModelScope.launch {
        when (format) {
            Bitmap.CompressFormat.PNG -> ".png"
            Bitmap.CompressFormat.JPEG -> ".jpg"
            else -> null
        }?.let { extension ->
            photoRepository.addNewPhoto(uri, extension)
        }
    }

    fun movePhoto(fromPosition: Int, toPosition: Int) = viewModelScope.launch {
        photoRepository.changePhotoOrder(fromPosition + 1, toPosition + 1)
    }

    fun enterDeleteMode() {
        setIsDeleteMode(true)
        setIsInstructionMode(false)
    }

    fun exitWithoutPhotoDeletion() {
        setIsDeleteMode(false)
        _markedAsDeletePhotos.update { listOf() }
    }

    fun exitWithPhotoDeletion() {
        setIsDeleteMode(false)
    }

    fun confirmDeletePhoto() = viewModelScope.launch {
        val photos = _markedAsDeletePhotos.value
        photoRepository.deletePhotos(photos)
        setIsDeleteMode(false)
    }

    fun selectPhotoToDelete(photo: PhotoData) {
        _markedAsDeletePhotos.update { it + photo }
    }

    fun unselectPhotoToDelete(photo: PhotoData) {
        _markedAsDeletePhotos.update { it.filter { deletePhoto -> deletePhoto.id != photo.id } }
    }

    fun enterInstructionMode() {
        setIsInstructionMode(true)
        setIsDeleteMode(false)
    }

    fun exitInstructionMode() {
        setIsInstructionMode(false)
    }

    private fun setIsDeleteMode(isDeleteMode: Boolean) {
        handle[IS_DELETE_MODE_KEY] = isDeleteMode
    }

    private fun setIsInstructionMode(isInstructionMode: Boolean) {
        handle[IS_INSTRUCTION_MODE_KEY] = isInstructionMode
    }
}
