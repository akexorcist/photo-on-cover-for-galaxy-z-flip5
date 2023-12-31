package com.akexorcist.photooncover.base.core.data

import com.akexorcist.photooncover.base.core.data.db.PhotoEntity

data class PhotoData(
    val id: String,
    val fileName: String,
    val order: Int,
    val markAsDelete: Boolean,
)

fun PhotoEntity.toData() = PhotoData(
    id = id,
    fileName = path,
    order = order,
    markAsDelete = false,
)
