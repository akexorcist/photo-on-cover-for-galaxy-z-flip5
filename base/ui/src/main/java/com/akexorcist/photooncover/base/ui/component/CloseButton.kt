package com.akexorcist.photooncover.base.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.photooncover.base.resource.R as ResourceR

@Composable
fun ExitButton(
    contentDescription: String,
    onClick: () -> Unit,
) {
    FilledIconButton(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
            )
            .size(36.dp),
        shape = MaterialTheme.shapes.medium,
        colors = IconButtonDefaults.filledIconButtonColors(),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(ResourceR.drawable.ic_cancel),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
private fun ExitButtonPreview() {
    ExitButton(
        contentDescription = "",
        onClick = {},
    )
}
