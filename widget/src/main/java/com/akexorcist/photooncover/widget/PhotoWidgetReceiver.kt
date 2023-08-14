package com.akexorcist.photooncover.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhotoWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    private val viewModel: PhotoViewModel by inject()

    override val glanceAppWidget: GlanceAppWidget = PhotoWidget(viewModel)
}
