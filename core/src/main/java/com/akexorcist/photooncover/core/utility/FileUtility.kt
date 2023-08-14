package com.akexorcist.photooncover.core.utility

import android.content.Context
import java.io.File

object FileUtility {
    fun getPhotoDirectory(context: Context): File {
        return File(context.filesDir, "photo").apply {
            if (!exists()) {
                mkdir()
            }
        }
    }
}
