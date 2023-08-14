package com.akexorcist.photooncover.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.akexorcist.photooncover.core.navigation.WidgetNavigation
import com.akexorcist.photooncover.widget.theme.PhotoWidgetTheme

class PhotoWidget(
    private val photoViewModel: PhotoViewModel,
    private val widgetNavigation: WidgetNavigation,
) : GlanceAppWidget() {
    private var lastGlanceId: GlanceId? = null

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            if (lastGlanceId == null || lastGlanceId != id) {
                lastGlanceId = id
                PhotoWidgetTheme {
                    PhotoRoute(
                        photoViewModel = photoViewModel,
                        widgetNavigation = widgetNavigation,
                    )
                }
            }
        }
    }

    override val sizeMode: SizeMode = SizeMode.Exact
}

//@OptIn(ExperimentalGlanceRemoteViewsApi::class)
//@Preview
//@Composable
//fun PhotoScreenPreview() {
//    val displaySize = DpSize(200.dp, 180.dp)
//    val viewModel = PhotoViewModel()
//    val instance = PhotoWidget(viewModel)
//    GlanceAppWidgetHostPreview(
//        modifier = Modifier.fillMaxSize(),
//        glanceAppWidget = instance,
//        displaySize = displaySize,
//    )
//}