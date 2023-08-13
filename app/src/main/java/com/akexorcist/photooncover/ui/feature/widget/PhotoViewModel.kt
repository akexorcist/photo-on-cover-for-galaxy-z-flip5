package com.akexorcist.photooncover.ui.feature.widget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PhotoViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<PhotoUiState> = MutableStateFlow(
        PhotoUiState(
            currentIndex = 0,
            photos = listOf(PhotoType.Sample)
        )
    )
    val uiState: StateFlow<PhotoUiState> = _uiState

    fun nextPhoto() {
        _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
    }

    fun previousPhoto() {
        _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
    }
}
