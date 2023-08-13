package com.akexorcist.photooncover.ui.feature.widget

data class PhotoUiState(
    val currentIndex: Int,
    val photos: List<PhotoType>,
)

sealed class PhotoType {
    object Sample : PhotoType()
}
