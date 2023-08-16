package com.akexorcist.photooncover.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akexorcist.photooncover.base.ui.component.DebouncedClickable
import com.akexorcist.photooncover.base.resource.R as ResourceR

@Composable
internal fun DeletePhotoConfirmationScreen(
    showConfirmPhotoDeletion: Boolean,
    verticalSlideAnimationOffset: Int,
    deleteCount: Int,
    onCancelDeleteClick: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = showConfirmPhotoDeletion,
        enter = fadeIn() + slideInVertically { verticalSlideAnimationOffset },
        exit = fadeOut() + slideOutVertically { verticalSlideAnimationOffset },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = pluralStringResource(ResourceR.plurals.confirm_deletion_title, deleteCount, deleteCount),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = stringResource(ResourceR.string.confirm_deletion_description),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(32.dp))
            OutlinedButton(
                modifier = Modifier.width(200.dp),
                onClick = onCancelDeleteClick,
            ) {
                Text(text = stringResource(ResourceR.string.confirm_deletion_cancel))
            }
            Spacer(modifier = Modifier.size(8.dp))
            DebouncedClickable(
                onClick = onConfirmDeleteClick
            ) { onClickHandler ->
                Button(
                    modifier = Modifier.width(200.dp),
                    onClick = onClickHandler,
                ) {
                    Text(text = pluralStringResource(ResourceR.plurals.confirm_deletion_confirm, deleteCount))
                }
            }
        }
    }
}
