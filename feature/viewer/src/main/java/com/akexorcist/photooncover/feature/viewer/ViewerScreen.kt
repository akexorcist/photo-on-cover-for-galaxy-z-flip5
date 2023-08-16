package com.akexorcist.photooncover.feature.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.akexorcist.photooncover.base.core.utility.FileUtility
import com.akexorcist.photooncover.base.ui.component.ExitButton
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
        onCloseViewerClick = { screenNavigator.navigateToHome() },
    )
}

@Composable
private fun ViewerScreen(
    fileName: String?,
    onCloseViewerClick: () -> Unit,
) {
    fileName ?: return
    val context = LocalContext.current
    val photoFile = File(FileUtility.getPhotoDirectory(context), fileName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        ZoomableAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoFile)
                .crossfade(300)
                .build(),
            contentDescription = stringResource(ResourceR.string.content_description_full_size),
        )
        Row(modifier = Modifier.padding(32.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            ExitButton(
                onClick = onCloseViewerClick,
                contentDescription = stringResource(ResourceR.string.content_description_close_instruction_button),
            )
        }
    }
}
