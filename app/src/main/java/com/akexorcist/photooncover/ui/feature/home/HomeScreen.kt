package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akexorcist.photooncover.R
import com.akexorcist.photooncover.core.config.PhotoHeightForGalaxyZFlip5
import com.akexorcist.photooncover.core.config.PhotoRatioForGalaxyZFlip5
import com.akexorcist.photooncover.core.config.PhotoWidthForGalaxyZFlip5
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.core.utility.FileUtility
import com.akexorcist.photooncover.ui.theme.PhotoOnCoverTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import org.koin.compose.koinInject
import java.io.File

@Suppress("PrivatePropertyName")
private val JpegCompressFormat = Bitmap.CompressFormat.JPEG

@Composable
fun HomeRoute(viewModel: HomeViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState(initial = HomeUiState(listOf()))
    var photoToAddUri: Uri? by remember { mutableStateOf(null) }

    val cropPhotoLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result -> result.uriContent?.let { viewModel.addPhoto(it, JpegCompressFormat) } },
    )
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { photoToAddUri = it } },
    )
    val colorScheme = MaterialTheme.colorScheme
    LaunchedEffect(photoToAddUri) {
        // Wait for photo picker complete collapse animation
        if (photoToAddUri == null) return@LaunchedEffect
        delay(500)
        cropPhotoLauncher.launch(
            CropImageContractOptions(
                uri = photoToAddUri,
                cropImageOptions = cropImageOption(
                    cropGuidelineColor = Color.White,
                    colorScheme = colorScheme,
                    compressFormat = JpegCompressFormat,
                ),
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
        onPhotoMoved = { fromPosition, toPosition ->
            viewModel.movePhoto(
                fromPosition = fromPosition,
                toPosition = toPosition,
            )
        },
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddPhotoClick: () -> Unit,
    onInstructionClick: () -> Unit,
    onPhotoMoved: (Int, Int) -> Unit,
) {
    val photos = uiState.photos
    LaunchedEffect(photos) {
        photos.forEach {
            Log.e("Check", "Photo: $it")
        }
    }
    Scaffold(
        topBar = { HomeTopBar(onInstructionClick = onInstructionClick) },
        floatingActionButton = { HomeFloatingActionButton(onClick = onAddPhotoClick) },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        HomeContent(
            padding = padding,
            photos = photos,
            onPhotoMoved = onPhotoMoved,
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
private fun HomeContent(
    padding: PaddingValues,
    photos: List<PhotoData>,
    onPhotoMoved: (Int, Int) -> Unit,
) {
    val context = LocalContext.current
    val state = rememberReorderableLazyGridState(
        onMove = { from, to -> onPhotoMoved(from.index, to.index) },
    )
    Box(modifier = Modifier.padding(padding)) {
        if (photos.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier
                    .reorderable(state)
                    .fillMaxSize()
                    .detectReorderAfterLongPress(state),
                columns = GridCells.Fixed(2),
                state = state.gridState,
                content = {
                    itemsIndexed(
                        items = photos,
                        key = { _, item -> item.id },
                    ) { index: Int, photo: PhotoData ->
                        ReorderableItem(state, photo.id) { isDragging ->
                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) 16.dp else 0.dp,
                                label = "photo_elevation_animation_at_$index"
                            )
                            val photoFile = File(FileUtility.getPhotoDirectory(context), photo.fileName)
                            PhotoItem(elevation, photoFile)
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
                Spacer(modifier = Modifier.size(8.dp))
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
                            text = "Add your first photo",
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
private fun PhotoItem(
    elevation: Dp,
    photoFile: File,
) {
    Box(modifier = Modifier.shadow(elevation)) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(ratio = PhotoRatioForGalaxyZFlip5)
                .clip(MaterialTheme.shapes.large),
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoFile)
                .crossfade(300)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
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
private fun HomeScreenPreview() {
    val uiState = HomeUiState(photos = listOf())
    PhotoOnCoverTheme(darkTheme = true) {
        HomeScreen(
            uiState = uiState,
            onAddPhotoClick = {},
            onInstructionClick = {},
            onPhotoMoved = { _, _ -> },
        )
    }
}

@Suppress("SameParameterValue")
private fun cropImageOption(
    cropGuidelineColor: Color,
    colorScheme: ColorScheme,
    compressFormat: Bitmap.CompressFormat,
) = CropImageOptions(
    fixAspectRatio = true,
    aspectRatioX = PhotoWidthForGalaxyZFlip5,
    aspectRatioY = PhotoHeightForGalaxyZFlip5,
    outputRequestWidth = PhotoWidthForGalaxyZFlip5,
    outputRequestHeight = PhotoHeightForGalaxyZFlip5,
    outputRequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_EXACT,
    backgroundColor = colorScheme.background.copy(alpha = 0.75f).toArgb(),
    activityBackgroundColor = colorScheme.background.toArgb(),
    outputCompressFormat = compressFormat,
    cropperLabelTextColor = colorScheme.onPrimaryContainer.toArgb(),
    activityMenuIconColor = colorScheme.onPrimaryContainer.toArgb(),
    activityMenuTextColor = colorScheme.onPrimaryContainer.toArgb(),
    toolbarBackButtonColor = colorScheme.onPrimaryContainer.toArgb(),
    toolbarTitleColor = colorScheme.onPrimaryContainer.toArgb(),
    toolbarColor = colorScheme.primaryContainer.toArgb(),
    progressBarColor = colorScheme.primary.toArgb(),
    guidelinesColor = cropGuidelineColor.toArgb(),
    borderCornerColor = cropGuidelineColor.toArgb(),
    borderLineColor = cropGuidelineColor.toArgb(),
)
