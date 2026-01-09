package com.example.game.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.game.ui.theme.GameTheme

@Composable
fun GameOverView(
    finalScore: Int,
    navController: NavController,
    viewModel: GameViewModel
) {
    val isNewRecord = viewModel.isNewRecord.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GAME OVER",
            fontSize = 50.sp,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (isNewRecord.value) {
            Text(
                text = "🏆 NEW RECORD!",
                fontSize = 30.sp,
                color = Color.Yellow,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        Text(
            text = "Score: $finalScore",
            fontSize = 40.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                viewModel.resetGame()
                navController.navigate("game") {
                    popUpTo("gameOver") { inclusive = true }
                }
            }
        ) {
            Text("Play Again")
        }

        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = false }
                }
            }
        ) {
            Text("Menu")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverViewPreview() {
    GameTheme {
        GameOverView(
            finalScore = 1000,
            navController = rememberNavController(),
            viewModel = GameViewModel()
        )
    }
}
