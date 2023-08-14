package com.akexorcist.photooncover.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.photooncover.core.data.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val photoRepository: PhotoRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<PhotoUiState> = MutableStateFlow(
        PhotoUiState(
            currentIndex = 0,
            photos = listOf()
        )
    )
    val uiState: StateFlow<PhotoUiState> = _uiState

    fun observePhotos() = viewModelScope.launch {
        photoRepository.getPhotos().collectLatest { photos ->
            _uiState.update { uiState -> uiState.copy(photos = photos) }
        }
    }

    fun nextPhoto() {
        _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
    }

    fun previousPhoto() {
        _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
    }
}
