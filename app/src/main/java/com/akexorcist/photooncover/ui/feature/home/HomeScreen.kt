@file:OptIn(ExperimentalMaterial3Api::class)

package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akexorcist.photooncover.R
import com.akexorcist.photooncover.config.PhotoHeightForGalaxyZFlip5
import com.akexorcist.photooncover.config.PhotoRatioForGalaxyZFlip5
import com.akexorcist.photooncover.config.PhotoWidthForGalaxyZFlip5
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.ui.theme.PhotoOnCoverTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun HomeRoute(viewModel: HomeViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState(initial = HomeUiState(listOf()))
    var photoToAddUri: Uri? by remember { mutableStateOf(null) }

    val cropPhotoLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result -> result.uriContent?.let { viewModel.addPhoto(it) } },
    )
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { photoToAddUri = it } },
    )
    LaunchedEffect(photoToAddUri) {
        // Wait for photo picker complete collapse animation
        if (photoToAddUri == null) return@LaunchedEffect
        delay(500)
        cropPhotoLauncher.launch(
            CropImageContractOptions(
                uri = photoToAddUri,
                cropImageOptions = CropImageOptions(
                    fixAspectRatio = true,
                    aspectRatioX = PhotoWidthForGalaxyZFlip5,
                    aspectRatioY = PhotoHeightForGalaxyZFlip5,
                    outputRequestWidth = PhotoWidthForGalaxyZFlip5,
                    outputRequestHeight = PhotoHeightForGalaxyZFlip5,
                    backgroundColor = Color.Black.copy(alpha = 0.75f).toArgb(),
                    activityBackgroundColor = Color.Black.toArgb(),
                    outputCompressFormat = Bitmap.CompressFormat.JPEG,
                )
            )
        )
        photoToAddUri = null
    }
    HomeScreen(
        uiState = uiState,
        onAddPhotoClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        onInstructionClick = {},
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddPhotoClick: () -> Unit,
    onInstructionClick: () -> Unit,
) {
    val photos = uiState.photos
    Scaffold(
        topBar = {
            HomeTopBar(
                onInstructionClick = onInstructionClick,
            )
        },
        floatingActionButton = { HomeFloatingActionButton(onClick = onAddPhotoClick) },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        HomeContent(
            padding = padding,
            photos = photos,
        )
    }
}

@Composable
private fun HomeTopBar(
    onInstructionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Photo on Cover",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onInstructionClick,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_instruction),
                contentDescription = "Instruction",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun HomeContent(padding: PaddingValues, photos: List<PhotoData>) {
    Box(modifier = Modifier.padding(padding)) {
        if (!photos.isNullOrEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    items(count = photos.size) { position ->
                        photos.getOrNull(position)?.let { photo ->
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(ratio = PhotoRatioForGalaxyZFlip5)
                                    .clip(FloatingActionButtonDefaults.shape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(photo.path)
                                    .crossfade(300)
                                    .build(),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                            )
                        }
                    }
                },
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 84.dp,
                    start = 8.dp,
                    end = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(R.drawable.ic_empty),
                    contentDescription = "Empty photo",
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = "Your photo on cover\nis empty",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                InfiniteBounceAnimation {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Add your first photo here",
                            fontWeight = FontWeight.Bold,
                        )
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(R.drawable.ic_down),
                            contentDescription = "Down arrow",
                        )
                        Spacer(modifier = Modifier.size(84.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfiniteBounceAnimation(
    content: @Composable () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_bounce_animation")
    val position by infiniteTransition.animateFloat(
        initialValue = -10.dp.value,
        targetValue = 10.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "infinite_bounce_animation",
    )
    Box(modifier = Modifier.offset(y = position.dp)) {
        content()
    }
}

@Composable
private fun HomeFloatingActionButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        shape = FloatingActionButtonDefaults.shape,
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "Add photo",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val uiState = HomeUiState(photos = listOf())
    PhotoOnCoverTheme(darkTheme = true) {
        HomeScreen(
            uiState = uiState,
            onAddPhotoClick = {},
            onInstructionClick = {},
        )
    }
}
