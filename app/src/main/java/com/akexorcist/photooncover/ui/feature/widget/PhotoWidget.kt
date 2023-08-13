package com.akexorcist.photooncover.ui.feature.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent

class PhotoWidget(private val photoViewModel: PhotoViewModel) : GlanceAppWidget() {
    private var lastGlanceId: GlanceId? = null

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            if (lastGlanceId == null || lastGlanceId != id) {
                lastGlanceId = id
                GlanceTheme {
                    Log.e("Check", "PhotoRoute $id")
                    PhotoRoute(photoViewModel = photoViewModel)
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