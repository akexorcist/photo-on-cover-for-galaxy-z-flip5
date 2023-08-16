package com.akexorcist.photooncover.base.core.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "order") val order: Int,
)
