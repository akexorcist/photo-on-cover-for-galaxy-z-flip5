package com.akexorcist.photooncover.feature.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.akexorcist.photooncover.base.core.utility.FileUtility
import com.akexorcist.photooncover.feature.viewer.navigation.rememberViewerScreenNavigator
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage
import java.io.File
import com.akexorcist.photooncover.base.resource.R as ResourceR

@Composable
fun ViewerRoute(
    navController: NavController,
    fileName: String?,
) {
    val screenNavigator = rememberViewerScreenNavigator(navController = navController)
    LaunchedEffect(fileName) {
        if (fileName == null) {
            screenNavigator.navigateToHome()
        }
    }

    ViewerScreen(
        fileName = fileName,
    )
}

@Composable
private fun ViewerScreen(fileName: String?) {
    fileName ?: return
    val context = LocalContext.current
    val photoFile = File(FileUtility.getPhotoDirectory(context), fileName)

    ZoomableAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoFile)
            .crossfade(300)
            .build(),
        contentDescription = stringResource(ResourceR.string.content_description_full_size),
    )
}
