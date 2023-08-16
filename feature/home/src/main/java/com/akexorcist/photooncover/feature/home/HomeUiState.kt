package com.akexorcist.photooncover.feature.home

import com.akexorcist.photooncover.base.core.data.PhotoData

sealed class HomeUiState(
    open val photos: List<PhotoData>,
) {
    data class ViewMode(
        override val photos: List<PhotoData>
    ) : HomeUiState(photos)

    data class DeleteMode(
        override val photos: List<PhotoData>,
    ) : HomeUiState(photos)

    data class InstructionMode(
        override val photos: List<PhotoData>,
    ) : HomeUiState(photos)

    fun update(photos: List<PhotoData>) = when (this) {
        is ViewMode -> this.copy(photos = photos)
        is DeleteMode -> this.copy(photos = photos)
        is InstructionMode -> this.copy(photos = photos)
    }

    companion object {
        fun perform(
            photos: List<PhotoData>,
            isDeleteMode: Boolean,
            isInstructionMode: Boolean,
        ) = when {
            isDeleteMode -> DeleteMode(photos)
            isInstructionMode -> InstructionMode(photos)
            else -> ViewMode(photos)
        }
    }
}
