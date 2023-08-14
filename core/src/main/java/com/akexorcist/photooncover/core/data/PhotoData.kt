package com.akexorcist.photooncover.core.data

import com.akexorcist.photooncover.core.data.db.PhotoEntity

data class PhotoData(
    val id: String?,
    val path: String?,
)

fun PhotoEntity.toData() = PhotoData(
    id = id,
    path = path,
)
