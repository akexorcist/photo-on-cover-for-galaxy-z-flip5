package com.akexorcist.photooncover.ui.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.akexorcist.photooncover.ui.theme.PhotoOnCoverTheme
import org.koin.compose.koinInject


@Composable
fun HomeRoute(viewModel: HomeViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(uiState)
}

@Composable
fun HomeScreen(uiState: HomeUiState) {
    Text(text = "Hello World!")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val uiState = HomeUiState("")
    PhotoOnCoverTheme {
        HomeScreen(uiState)
    }
}
