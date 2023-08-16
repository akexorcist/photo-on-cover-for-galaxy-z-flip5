package com.akexorcist.photooncover.feature.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.akexorcist.photooncover.base.core.navigation.WidgetScreenNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhotoWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    private val viewModel: PhotoViewModel by inject()
    private val widgetScreenNavigator: WidgetScreenNavigator by inject()

    override val glanceAppWidget: GlanceAppWidget = PhotoWidget(
        photoViewModel = viewModel,
        widgetScreenNavigator = widgetScreenNavigator,
    )

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeWidgetReload(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ReloadWidgetCallback.RELOAD_ACTION) {
            observeWidgetReload(context)
        }
    }

    private fun observeWidgetReload(context: Context) = coroutineScope.launch {
        GlanceAppWidgetManager(context).getGlanceIds(PhotoWidget::class.java).firstOrNull()?.let { glanceId ->
            glanceAppWidget.update(context, glanceId)
        }
    }
}
