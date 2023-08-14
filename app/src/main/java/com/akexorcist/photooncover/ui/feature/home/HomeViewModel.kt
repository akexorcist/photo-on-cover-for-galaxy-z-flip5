package com.akexorcist.photooncover.ui.feature.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.core.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    fun addPhoto(uri: Uri) = viewModelScope.launch {
        val result = photoRepository.addNewPhoto(uri)
    }
}
