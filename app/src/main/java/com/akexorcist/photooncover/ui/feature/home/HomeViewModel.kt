package com.akexorcist.photooncover.ui.feature.home

import androidx.lifecycle.ViewModel
import com.akexorcist.photooncover.data.PhotoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            photos = (0 until 10).map {
                PhotoData(
                    label = it.toString().padStart(3, '0'),
                    path = "https://picsum.photos/748/654",
                )
            }
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState
}
