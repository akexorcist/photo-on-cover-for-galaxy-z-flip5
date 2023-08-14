package com.akexorcist.photooncover.widget

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.core.navigation.WidgetNavigation
import com.akexorcist.photooncover.core.utility.FileUtility
import java.io.File

@Suppress("PrivatePropertyName")
private val BottomBarWidth = 192.dp

@Suppress("PrivatePropertyName")
private val BottomBarHeight = 31.dp

@Composable
fun PhotoRoute(photoViewModel: PhotoViewModel, widgetNavigation: WidgetNavigation) {
    val uiState by photoViewModel.uiState.collectAsState()
    val isPreviousPhotoAvailable = uiState.currentIndex > 0
    val isNextPhotoAvailable = uiState.currentIndex < uiState.photos.size - 1
    val photo = uiState.photos.getOrNull(uiState.currentIndex)
    val context = LocalContext.current

    PhotoScreen(
        photo = photo,
        isPreviousPhotoAvailable = isPreviousPhotoAvailable,
        isNextPhotoAvailable = isNextPhotoAvailable,
        onPreviousPhotoClick = { photoViewModel.previousPhoto() },
        onNextPhotoClick = { photoViewModel.nextPhoto() },
        onOpenAppClick = { widgetNavigation.navigateToMain(context) },
        onReloadClick = { actionRunCallback(ReloadWidgetCallback::class.java) },
    )

    LaunchedEffect(Unit) { photoViewModel.observePhotos() }
}

@Composable
fun PhotoScreen(
    photo: PhotoData?,
    isPreviousPhotoAvailable: Boolean,
    isNextPhotoAvailable: Boolean,
    onPreviousPhotoClick: () -> Unit,
    onNextPhotoClick: () -> Unit,
    onOpenAppClick: () -> Unit,
    onReloadClick: () -> Unit,
) {
    Log.e("Check", "Path ${photo?.path}")
    val context = LocalContext.current
    photo?.path
        ?.let { path ->
            FileProvider.getUriForFile(
                LocalContext.current,
                "com.akexorcist.photooncover.fileprovider",
                File(FileUtility.getPhotoDirectory(context), path),
            )
        }
//        ?.let { path ->
//            Uri.fromFile(File(FileUtility.getPhotoDirectory(context), path))
//        }
        ?.let { photoUri ->
            Log.e("Check", "Photo URI $photoUri")
            PhotoContent(
                photoUri = photoUri,
                isPreviousPhotoAvailable = isPreviousPhotoAvailable,
                isNextPhotoAvailable = isNextPhotoAvailable,
                onPreviousPhotoClick = onPreviousPhotoClick,
                onNextPhotoClick = onNextPhotoClick,
            )
        }
        ?: run {
            NoPhotoAvailable(
                onOpenAppClick = onOpenAppClick,
                onReloadClick = onReloadClick,
            )
        }
}

@Composable
private fun PhotoContent(
    photoUri: Uri,
    isPreviousPhotoAvailable: Boolean,
    isNextPhotoAvailable: Boolean,
    onPreviousPhotoClick: () -> Unit,
    onNextPhotoClick: () -> Unit,
) {
    Column {
        PhotoCard(
            modifier = GlanceModifier.defaultWeight(),
            photoUri = photoUri,
        )
        BottomBar(
            isPreviousPhotoAvailable = isPreviousPhotoAvailable,
            isNextPhotoAvailable = isNextPhotoAvailable,
            onPreviousPhotoClick = onPreviousPhotoClick,
            onNextPhotoClick = onNextPhotoClick,
        )
    }
}

@Composable
private fun BottomBar(
    isPreviousPhotoAvailable: Boolean,
    isNextPhotoAvailable: Boolean,
    onPreviousPhotoClick: () -> Unit,
    onNextPhotoClick: () -> Unit,
) {
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(BottomBarHeight)
            .background(GlanceTheme.colors.background)
    ) {
        Row(
            modifier = GlanceModifier
                .width(BottomBarWidth)
                .height(BottomBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = GlanceModifier
                    .width(56.dp)
                    .height(BottomBarHeight)
            ) {
                if (isPreviousPhotoAvailable) {
                    ImageButton(
                        provider = ImageProvider(R.drawable.ic_backward),
                        onClick = { onPreviousPhotoClick() },
                    )
                }
            }
            Spacer(modifier = GlanceModifier.width(8.dp))
            Box(modifier = GlanceModifier.width(56.dp).height(BottomBarHeight)) {
                if (isNextPhotoAvailable) {
                    ImageButton(
                        provider = ImageProvider(R.drawable.ic_forward),
                        onClick = { onNextPhotoClick() },
                    )
                }
            }
            Spacer(modifier = GlanceModifier.width(32.dp))
        }
    }
}

@Composable
fun NoPhotoAvailable(
    onOpenAppClick: () -> Unit,
    onReloadClick: () -> Unit,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "No photo available",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
            )
        )
        Spacer(modifier = GlanceModifier.size(16.dp))
        Button(
            text = "Select from app",
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GlanceTheme.colors.primary,
                contentColor = GlanceTheme.colors.onPrimary,
            ),
            onClick = onOpenAppClick,
        )
        Spacer(modifier = GlanceModifier.size(16.dp))
        Button(
            text = "Reload",
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GlanceTheme.colors.primary,
                contentColor = GlanceTheme.colors.onPrimary,
            ),
            onClick = onReloadClick,
        )
    }
}

@Composable
private fun PhotoCard(
    modifier: GlanceModifier = GlanceModifier,
    photoUri: Uri,
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                provider = ImageProvider(photoUri),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun ImageButton(
    provider: ImageProvider,
    onClick: () -> Unit,
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = GlanceModifier.size(20.dp),
            provider = provider,
            contentDescription = null,
        )
    }
}
