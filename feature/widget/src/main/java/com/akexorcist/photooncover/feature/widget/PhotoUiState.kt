package com.akexorcist.photooncover.feature.widget

import com.akexorcist.photooncover.base.core.data.PhotoData

data class PhotoUiState(
    val currentIndex: Int,
    val photos: List<PhotoData>,
)
