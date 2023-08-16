package com.akexorcist.photooncover.widget

import com.akexorcist.photooncover.base.core.data.PhotoData

data class PhotoUiState(
    val currentIndex: Int,
    val photos: List<PhotoData>,
)
