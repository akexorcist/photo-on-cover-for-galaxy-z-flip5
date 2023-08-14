package com.akexorcist.photooncover.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class ReloadWidgetCallback : ActionCallback {

    companion object {
        const val RELOAD_ACTION = "ACTION_TRIGGER_LAMBDA"
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(context, PhotoWidgetReceiver::class.java).apply {
            this.action = RELOAD_ACTION
        }
        context.sendBroadcast(intent)
    }
}
