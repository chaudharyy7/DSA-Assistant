package com.example.codenation.splashscreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.*
import com.example.codenation.home.HomeScreen

@Composable
fun AppEntryPoint() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
    } else {
        HomeScreen(padding = PaddingValues())
    }
}
