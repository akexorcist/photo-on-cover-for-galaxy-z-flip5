package com.akexorcist.photooncover.core.data

import com.akexorcist.photooncover.core.data.db.PhotoEntity

data class PhotoData(
    val id: String,
    val fileName: String,
    val order: Int,
)

fun PhotoEntity.toData() = PhotoData(
    id = id,
    fileName = path,
    order = order,
)
