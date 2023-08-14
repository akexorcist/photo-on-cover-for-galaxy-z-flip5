package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.photooncover.core.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {
    val uiState: Flow<HomeUiState> = photoRepository.getPhotos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf(),
        ).map {
            HomeUiState(
                photos = it
            )
        }

    fun addPhoto(uri: Uri, format: Bitmap.CompressFormat) = viewModelScope.launch {
        when (format) {
            Bitmap.CompressFormat.PNG -> ".png"
            Bitmap.CompressFormat.JPEG -> ".jpg"
            else -> null
        }?.let { extension ->
            val result = photoRepository.addNewPhoto(uri, extension)
            // TODO
        }
    }
}
