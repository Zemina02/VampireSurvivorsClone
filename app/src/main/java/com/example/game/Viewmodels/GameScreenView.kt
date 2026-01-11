package com.example.game.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun GameScreenView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GameViewModel
) {
    val configuration = LocalConfiguration.current
    val density = configuration.densityDpi / 160f
    val screenWidthPx = configuration.screenWidthDp * density
    val screenHeightPx = configuration.screenHeightDp * density

    val isGameOver = viewModel.isGameOver.collectAsState()

    val context = LocalContext.current

    val game = remember {
        Game(
            context,
            screenWidthPx.toInt(),
            screenHeightPx.toInt(),
            viewModel
        )
    }

    LaunchedEffect(isGameOver.value) {
        if (isGameOver.value) {
            navController.navigate("gameOver/${viewModel.finalScore.value}") {
                popUpTo("game") { inclusive = true }
            }
        }
    }

    DisposableEffect(Unit) {
        game.resume()
        onDispose {
            game.pause()
        }
    }

    AndroidView(
        factory = { game },
        modifier = modifier.fillMaxSize()
    )
}
