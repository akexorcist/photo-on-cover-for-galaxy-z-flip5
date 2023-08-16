package com.akexorcist.photooncover.navigation

import android.content.Context
import android.content.Intent
import com.akexorcist.photooncover.base.core.navigation.WidgetNavigation
import com.akexorcist.photooncover.ui.MainActivity

class DefaultWidgetNavigation : WidgetNavigation {
    override fun navigateToMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        // There's an issue about this statement
        // See https://stackoverflow.com/a/76480070
//        actionStartActivity<MainActivity>()
    }
}
