package com.akexorcist.photooncover.ui.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState("")
    )
    val uiState: StateFlow<HomeUiState> = _uiState
}
