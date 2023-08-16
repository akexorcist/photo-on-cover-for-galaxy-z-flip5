package com.akexorcist.photooncover.feature.instruction

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.akexorcist.photooncover.base.core.utility.toPx
import com.akexorcist.photooncover.base.ui.component.ExitButton
import com.akexorcist.photooncover.feature.instruction.navigation.rememberInstructionScreenNavigator
import com.akexorcist.photooncover.base.resource.R as ResourceR

@Composable
fun InstructionRoute(
    navController: NavController
) {
    val screenNavigator = rememberInstructionScreenNavigator(navController = navController)
    val scrollState = rememberScrollState()

    InstructionScreen(
        scrollState = scrollState,
        onCloseInstructionClick = {
            screenNavigator.navigateToHome()
        },
    )
}

@Composable
private fun InstructionScreen(
    scrollState: ScrollState,
    onCloseInstructionClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.size(96.dp))
            InstructionText(
                bullet = ResourceR.string.instruction_1_bullet,
                detail = ResourceR.string.instruction_1_detail,
            )
            InstructionImage(
                image = R.drawable.image_instruction_add,
                contentDescription = ResourceR.string.content_description_add_new_photo,
            )
            InstructionText(
                bullet = ResourceR.string.instruction_2_bullet,
                detail = ResourceR.string.instruction_2_detail,
            )
            InstructionText(
                bullet = ResourceR.string.instruction_3_bullet,
                detail = ResourceR.string.instruction_3_detail,
            )
            InstructionImage(
                image = R.drawable.image_instruction_widget,
                contentDescription = ResourceR.string.content_description_add_widget_on_cover_screen,
            )
            InstructionText(
                bullet = ResourceR.string.instruction_4_bullet,
                detail = ResourceR.string.instruction_4_detail,
            )
            InstructionImage(
                image = R.drawable.image_instruction_completed,
                contentDescription = ResourceR.string.content_description_widget_added,
            )
            Spacer(modifier = Modifier.size(32.dp))
        }
        Row(modifier = Modifier.padding(32.dp)) {
            Spacer(modifier = Modifier.weight(1f))
            ExitButton(
                onClick = onCloseInstructionClick,
                contentDescription = stringResource(ResourceR.string.content_description_close_instruction_button),
            )
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
        InstructionScreen(
            scrollState = rememberScrollState(),
            onCloseInstructionClick = {},
        )
    }
}
