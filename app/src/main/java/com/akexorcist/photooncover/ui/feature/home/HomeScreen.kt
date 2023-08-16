package com.akexorcist.photooncover.ui.feature.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akexorcist.photooncover.R
import com.akexorcist.photooncover.core.config.PhotoHeightForGalaxyZFlip5
import com.akexorcist.photooncover.core.config.PhotoRatioForGalaxyZFlip5
import com.akexorcist.photooncover.core.config.PhotoWidthForGalaxyZFlip5
import com.akexorcist.photooncover.core.data.PhotoData
import com.akexorcist.photooncover.core.utility.FileUtility
import com.akexorcist.photooncover.core.utility.toPx
import com.akexorcist.photooncover.ui.feature.home.navigation.rememberHomeScreenNavigator
import com.akexorcist.photooncover.ui.theme.PhotoOnCoverTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyGridState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Suppress("PrivatePropertyName")
private val DefaultPhotoItemShape = RoundedCornerShape(16.dp)

@Suppress("PrivatePropertyName")
private val JpegCompressFormat = Bitmap.CompressFormat.JPEG

@Composable
fun HomeRoute(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val screenNavigator = rememberHomeScreenNavigator(navController = navController)
    val uiState by viewModel.uiState.collectAsState(initial = HomeUiState.ViewMode(photos = listOf()))
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
        onDeleteClick = { viewModel.enterDeleteMode() },
        selectPhotoToDelete = { viewModel.selectPhotoToDelete(it) },
        unselectPhotoToDelete = { viewModel.unselectPhotoToDelete(it) },
        onExitDeleteModeClick = { viewModel.exitWithoutPhotoDeletion() },
        onPerformPhotoDeletionClick = { viewModel.exitWithPhotoDeletion() },
        onCancelDeleteClick = { viewModel.exitWithoutPhotoDeletion() },
        onConfirmDeleteClick = { viewModel.confirmDeletePhoto() },
        onInstructionClick = { viewModel.enterInstructionMode() },
        onCloseInstructionClick = { viewModel.exitInstructionMode() },
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
    onDeleteClick: () -> Unit,
    selectPhotoToDelete: (PhotoData) -> Unit,
    unselectPhotoToDelete: (PhotoData) -> Unit,
    onExitDeleteModeClick: () -> Unit,
    onPerformPhotoDeletionClick: () -> Unit,
    onCancelDeleteClick: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
    onInstructionClick: () -> Unit,
    onCloseInstructionClick: () -> Unit,
    onPhotoMoved: (Int, Int) -> Unit,
) {
    val photos = uiState.photos
    val isDeleteMode = uiState is HomeUiState.DeleteMode
    val isInstructionMode = uiState is HomeUiState.InstructionMode
    val deleteCount = photos.count { photo -> photo.markAsDelete }
    val showConfirmPhotoDeletion = !isDeleteMode && deleteCount > 0
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    isDeleteMode = isDeleteMode,
                    onDeleteClick = onDeleteClick,
                    onPerformPhotoDeletionClick = onPerformPhotoDeletionClick,
                    onExitDeleteModeClick = onExitDeleteModeClick,
                    onInstructionClick = onInstructionClick
                )
            },
            floatingActionButton = { HomeFloatingActionButton(onClick = onAddPhotoClick) },
            floatingActionButtonPosition = FabPosition.Center,
        ) { padding ->
            HomeContent(
                padding = padding,
                photos = photos,
                isDeleteMode = isDeleteMode,
                showConfirmPhotoDeletion = showConfirmPhotoDeletion,
                onPhotoMoved = onPhotoMoved,
                selectPhotoToDelete = selectPhotoToDelete,
                unselectPhotoToDelete = unselectPhotoToDelete,
            )
        }
        val verticalSlideAnimationOffset = 20.dp.toPx().toInt()
        DeletePhotoConfirmationScreen(
            showConfirmPhotoDeletion = showConfirmPhotoDeletion,
            verticalSlideAnimationOffset = verticalSlideAnimationOffset,
            deleteCount = deleteCount,
            onCancelDeleteClick = onCancelDeleteClick,
            onConfirmDeleteClick = onConfirmDeleteClick,
        )
        InstructionScreen(
            isInstructionMode = isInstructionMode,
            verticalSlideAnimationOffset = verticalSlideAnimationOffset,
            onCloseInstructionClick = onCloseInstructionClick,
        )
    }
}

@Composable
private fun HomeTopBar(
    isDeleteMode: Boolean,
    onDeleteClick: () -> Unit,
    onPerformPhotoDeletionClick: () -> Unit,
    onExitDeleteModeClick: () -> Unit,
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
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        HomeTopBarMenu(
            isDeleteMode = isDeleteMode,
            onDeleteClick = onDeleteClick,
            onPerformPhotoDeletionClick = onPerformPhotoDeletionClick,
            onExitDeleteModeClick = onExitDeleteModeClick,
            onInstructionClick = onInstructionClick,
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun HomeTopBarMenu(
    isDeleteMode: Boolean,
    onDeleteClick: () -> Unit,
    onPerformPhotoDeletionClick: () -> Unit,
    onExitDeleteModeClick: () -> Unit,
    onInstructionClick: () -> Unit,
) {
    Box(contentAlignment = Alignment.CenterEnd) {
        AnimatedVisibility(
            visible = !isDeleteMode,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.content_description_delete_menu),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onInstructionClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_instruction),
                        contentDescription = stringResource(R.string.content_description_instruction_menu),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isDeleteMode,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onExitDeleteModeClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = stringResource(R.string.content_description_exit_delete_mode_button),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onPerformPhotoDeletionClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_confirm),
                        contentDescription = stringResource(R.string.content_description_perform_photo_deletion_button),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    padding: PaddingValues,
    photos: List<PhotoData>,
    isDeleteMode: Boolean,
    showConfirmPhotoDeletion: Boolean,
    onPhotoMoved: (Int, Int) -> Unit,
    selectPhotoToDelete: (PhotoData) -> Unit,
    unselectPhotoToDelete: (PhotoData) -> Unit,
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
                        val photoFile = File(FileUtility.getPhotoDirectory(context), photo.fileName)
                        val itemPadding by animateDeleteModePhotoPadding(isDeleteMode)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(itemPadding)
                        ) {
                            if (isDeleteMode || showConfirmPhotoDeletion) {
                                DeleteModePhotoItem(
                                    photo = photo,
                                    unselectPhotoToDelete = unselectPhotoToDelete,
                                    selectPhotoToDelete = selectPhotoToDelete,
                                    photoFile = photoFile,
                                )
                            } else {
                                ViewModePhotoItem(state, photo, index, photoFile)
                            }
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
                    contentDescription = stringResource(R.string.content_description_empty_photo_icon),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.empty_photo),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                InfiniteBounceAnimation {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.add_first_photo),
                            fontWeight = FontWeight.Bold,
                        )
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(R.drawable.ic_down),
                            contentDescription = stringResource(R.string.content_description_down_arrow_icon),
                        )
                        Spacer(modifier = Modifier.size(84.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyGridItemScope.ViewModePhotoItem(
    state: ReorderableLazyGridState,
    photo: PhotoData,
    index: Int,
    photoFile: File
) {
    ReorderableItem(state, photo.id) { isDragging ->
        val elevation by animateDpAsState(
            targetValue = if (isDragging) 8.dp else 2.dp,
            label = "photo_elevation_animation_at_$index"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = elevation,
                    shape = DefaultPhotoItemShape,
                )
                .clip(DefaultPhotoItemShape)
        ) {
            PhotoItem(
                photoFile = photoFile,
            )
        }
    }
}

@Composable
private fun DeleteModePhotoItem(
    photo: PhotoData,
    unselectPhotoToDelete: (PhotoData) -> Unit,
    selectPhotoToDelete: (PhotoData) -> Unit,
    photoFile: File
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 2.dp,
                shape = DefaultPhotoItemShape,
            )
            .clip(DefaultPhotoItemShape)
            .clickable {
                if (photo.markAsDelete) unselectPhotoToDelete(photo)
                else selectPhotoToDelete(photo)
            }
    ) {
        PhotoItem(
            photoFile = photoFile,
        )
        if (photo.markAsDelete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            )
        }
        MarkedAsDeleteDimBackground(
            visible = photo.markAsDelete,
        )
        MarkedAsDeleteIcon(
            visible = photo.markAsDelete
        )
    }
}

@Composable
private fun MarkedAsDeleteIcon(visible: Boolean) {
    MarkedAsDeleteIconAnimatedVisibility(
        visible = visible,
        transformOrigin = TransformOrigin(0.8f, 0.4f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopEnd,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .size(36.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_selected),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = stringResource(R.string.content_description_marked_as_delete_icon),
                )
            }
        }
    }
}

@Composable
private fun animateDeleteModePhotoPadding(isDeleteMode: Boolean) = animateDpAsState(
    targetValue = if (isDeleteMode) 8.dp else 2.dp,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
    label = "delete_mode_padding",
)

@Composable
private fun PhotoItem(
    photoFile: File,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(ratio = PhotoRatioForGalaxyZFlip5),
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoFile)
            .crossfade(300)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Composable
private fun MarkedAsDeleteDimBackground(
    visible: Boolean,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                )
                .fillMaxWidth()
                .aspectRatio(PhotoRatioForGalaxyZFlip5)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MarkedAsDeleteIconAnimatedVisibility(
    visible: Boolean,
    transformOrigin: TransformOrigin,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(
            transformOrigin = transformOrigin,
        ),
        exit = fadeOut() + scaleOut(
            transformOrigin = transformOrigin,
        ),
    ) {
        content()
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
        elevation = FloatingActionButtonDefaults.elevation(2.dp),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(R.string.content_description_add_photo_button),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val uiState = HomeUiState.ViewMode(
        photos = listOf(),
    )
    PhotoOnCoverTheme(darkTheme = true) {
        HomeScreen(
            uiState = uiState,
            onAddPhotoClick = {},
            onDeleteClick = {},
            selectPhotoToDelete = {},
            unselectPhotoToDelete = {},
            onPerformPhotoDeletionClick = {},
            onExitDeleteModeClick = {},
            onCancelDeleteClick = {},
            onConfirmDeleteClick = {},
            onInstructionClick = {},
            onCloseInstructionClick = {},
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
