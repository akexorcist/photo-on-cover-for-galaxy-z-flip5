package com.akexorcist.photooncover.feature.widget.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import com.akexorcist.photooncover.base.ui.theme.Pink40
import com.akexorcist.photooncover.base.ui.theme.Pink80
import com.akexorcist.photooncover.base.ui.theme.Purple40
import com.akexorcist.photooncover.base.ui.theme.Purple80
import com.akexorcist.photooncover.base.ui.theme.PurpleGrey40
import com.akexorcist.photooncover.base.ui.theme.PurpleGrey80

@Suppress("PrivatePropertyName")
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

@Suppress("PrivatePropertyName")
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)

@Composable
fun PhotoWidgetTheme(
    content: @Composable () -> Unit,
) {
    val colors = ColorProviders(
        light = DarkColorScheme,
        dark = DarkColorScheme,
    )

    GlanceTheme(
        colors = colors,
        content = content
    )
}
