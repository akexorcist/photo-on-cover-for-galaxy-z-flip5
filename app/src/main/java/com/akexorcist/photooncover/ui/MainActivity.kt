package com.akexorcist.photooncover.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.akexorcist.photooncover.ui.feature.home.HomeRoute
import com.akexorcist.photooncover.ui.theme.PhotoOnCoverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoOnCoverTheme {
                HomeRoute()
            }
        }
    }
}
