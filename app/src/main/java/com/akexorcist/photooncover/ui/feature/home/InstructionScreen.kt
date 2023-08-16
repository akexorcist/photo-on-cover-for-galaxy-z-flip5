package com.akexorcist.photooncover.ui.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.photooncover.R
import com.akexorcist.photooncover.core.utility.toPx

@Composable
internal fun InstructionScreen(
    isInstructionMode: Boolean,
    verticalSlideAnimationOffset: Int,
    onCloseInstructionClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    AnimatedVisibility(
        visible = isInstructionMode,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { verticalSlideAnimationOffset }
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { verticalSlideAnimationOffset }
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { }
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .verticalScroll(scrollState),
            ) {
                Spacer(modifier = Modifier.size(96.dp))
                InstructionText(
                    bullet = R.string.instruction_1_bullet,
                    detail = R.string.instruction_1_detail,
                )
                InstructionImage(
                    R.drawable.image_instruction_add,
                    R.string.content_description_add_new_photo,
                )
                InstructionText(
                    bullet = R.string.instruction_2_bullet,
                    detail = R.string.instruction_2_detail,
                )
                InstructionText(
                    bullet = R.string.instruction_3_bullet,
                    detail = R.string.instruction_3_detail,
                )
                InstructionImage(
                    R.drawable.image_instruction_widget,
                    R.string.content_description_add_widget_on_cover_screen,
                )
                InstructionText(
                    bullet = R.string.instruction_4_bullet,
                    detail = R.string.instruction_4_detail,
                )
                InstructionImage(
                    R.drawable.image_instruction_completed,
                    R.string.content_description_widget_added,
                )
                Spacer(modifier = Modifier.size(32.dp))
            }
            Row(
                modifier = Modifier.padding(32.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                FilledIconButton(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = MaterialTheme.shapes.medium,
                        )
                        .size(36.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = IconButtonDefaults.filledIconButtonColors(),
                    onClick = onCloseInstructionClick,
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(R.drawable.ic_cancel),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(R.string.content_description_close_instruction_button),
                    )
                }
            }
        }
    }
}

@Composable
private fun InstructionImage(
    @DrawableRes image: Int,
    @StringRes contentDescription: Int,
) {
    Spacer(modifier = Modifier.size(16.dp))
    Image(
        modifier = Modifier
            .padding(
                start = 32.dp,
                end = 8.dp,
            )
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .wrapContentHeight(),
        painter = painterResource(image),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(contentDescription),
    )
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
private fun InstructionText(
    @StringRes bullet: Int,
    @StringRes detail: Int,
) {
    Spacer(modifier = Modifier.size(16.dp))
    Row {
        Text(
            text = stringResource(bullet),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(detail),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InstructionScreenPreview() {
    MaterialTheme {
        val offset = 20.dp.toPx()
        InstructionScreen(
            isInstructionMode = true,
            verticalSlideAnimationOffset = offset.toInt(),
            onCloseInstructionClick = {},
        )
    }
}
