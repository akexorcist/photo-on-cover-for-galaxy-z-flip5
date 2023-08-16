package com.akexorcist.photooncover.base.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncedClickable(
    delayMillis: Long = 300,
    onClick: () -> Unit,
    content: @Composable (onClickHandler: () -> Unit) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isClickable by remember { mutableStateOf(true) }

    val debouncedClick: () -> Unit = {
        if (isClickable) {
            isClickable = false
            coroutineScope.launch {
                delay(delayMillis)
                isClickable = true
            }
            onClick()
        }
    }

    content(debouncedClick)
}
