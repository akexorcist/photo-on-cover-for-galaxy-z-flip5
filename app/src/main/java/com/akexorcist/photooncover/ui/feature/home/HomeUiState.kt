package com.akexorcist.photooncover.ui.feature.home

import com.akexorcist.photooncover.core.data.PhotoData

sealed class HomeUiState(
    open val photos: List<PhotoData>,
) {
    data class ViewMode(
        override val photos: List<PhotoData>
    ) : HomeUiState(photos)

    data class DeleteMode(
        override val photos: List<PhotoData>,
    ) : HomeUiState(photos)

    fun update(photos: List<PhotoData>) = when (this) {
        is ViewMode -> this.copy(photos = photos)
        is DeleteMode -> this.copy(photos = photos)
    }

    companion object {
        fun perform(photos: List<PhotoData>, isDeleteMode: Boolean) = when (isDeleteMode) {
            true -> DeleteMode(photos)
            false -> ViewMode(photos)
        }
    }
}
