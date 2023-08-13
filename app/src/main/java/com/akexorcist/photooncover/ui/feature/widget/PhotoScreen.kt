package com.akexorcist.photooncover.ui.feature.widget

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
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
import androidx.glance.unit.ColorProvider
import com.akexorcist.photooncover.R

@Suppress("PrivatePropertyName")
private val BottomBarWidth = 192.dp

@Suppress("PrivatePropertyName")
private val BottomBarHeight = 31.dp

@Composable
fun PhotoRoute(photoViewModel: PhotoViewModel) {
    val uiState by photoViewModel.uiState.collectAsState()
    val isPreviousPhotoAvailable = uiState.currentIndex > 0
    val isNextPhotoAvailable = uiState.currentIndex < uiState.photos.size - 1
    val currentPhoto = uiState.photos.getOrNull(uiState.currentIndex)

    PhotoScreen(
        photo = currentPhoto,
        isPreviousPhotoAvailable = isPreviousPhotoAvailable,
        isNextPhotoAvailable = isNextPhotoAvailable,
        onPreviousPhotoClick = {
            photoViewModel.previousPhoto()
        },
        onNextPhotoClick = {
            photoViewModel.nextPhoto()
        },
    )
}

@Composable
fun PhotoScreen(
    photo: PhotoType?,
    isPreviousPhotoAvailable: Boolean,
    isNextPhotoAvailable: Boolean,
    onPreviousPhotoClick: () -> Unit,
    onNextPhotoClick: () -> Unit,
) {
    val resourceId = when (photo) {
        is PhotoType.Sample -> R.drawable.ic_qr_sample
        else -> null
    }
    if (resourceId != null) {
        PhotoContent(
            resourceId = resourceId,
            isPreviousPhotoAvailable = isPreviousPhotoAvailable,
            isNextPhotoAvailable = isNextPhotoAvailable,
            onPreviousPhotoClick = onPreviousPhotoClick,
            onNextPhotoClick = onNextPhotoClick,
        )
    } else {
        NoPhotoAvailable()
    }
}

@Composable
private fun PhotoContent(
    resourceId: Int,
    isPreviousPhotoAvailable: Boolean,
    isNextPhotoAvailable: Boolean,
    onPreviousPhotoClick: () -> Unit,
    onNextPhotoClick: () -> Unit,
) {
    Column {
        PhotoCard(
            modifier = GlanceModifier.defaultWeight(),
            imageResourceId = resourceId,
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
            .background(Color.Black)
    ) {
        Row(
            modifier = GlanceModifier
                .width(BottomBarWidth)
                .height(BottomBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = GlanceModifier.width(56.dp).height(BottomBarHeight)) {
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
fun NoPhotoAvailable() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "No photo available", style = TextStyle(color = ColorProvider(Color.White)))
        Spacer(modifier = GlanceModifier.size(16.dp))
        Button(text = "Select from app", onClick = { Log.e("Check", "Open app") })
    }
}

@Composable
private fun PhotoCard(
    modifier: GlanceModifier = GlanceModifier,
    @DrawableRes imageResourceId: Int,
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
                provider = ImageProvider(imageResourceId),
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
