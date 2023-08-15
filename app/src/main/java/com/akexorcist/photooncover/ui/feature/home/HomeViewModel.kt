package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.photooncover.core.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _isEditing = MutableStateFlow(false)

    val uiState: Flow<HomeUiState> = photoRepository.getPhotos().combine(_isEditing) { photos, isEditing ->
        HomeUiState(
            photos = photos,
            isDeleteMode = isEditing,
        )
    }
//    val uiState: Flow<HomeUiState> = photoRepository.getPhotos()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = listOf(),
//        ).map {
//            HomeUiState(
//                photos = it,
//                isEditing = _isEditing.,
//            )
//        }

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
        _isEditing.update { true }
    }

    fun exitDeleteMode() {
        _isEditing.update { false }
    }
}
