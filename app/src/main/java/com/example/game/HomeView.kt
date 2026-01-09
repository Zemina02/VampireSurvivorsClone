package com.example.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.game.components.MyTopBar
import com.example.game.ui.theme.GameTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            MyTopBar(
                topBarTitle = "Game Menu",
                isHomeScreen = true,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎮 GAME MENU", fontSize = 40.sp, modifier = Modifier.padding(16.dp))

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { navController.navigate("game") }
            ) {
                Text("PLAY", fontSize = 18.sp)
            }

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { navController.navigate("highscore") }
            ) {
                Text("MY HIGHSCORE", fontSize = 18.sp)
            }

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { navController.navigate("profile") }
            ) {
                Text("PROFILE", fontSize = 18.sp)
            }

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Text("LOGOUT", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    GameTheme {
        HomeView(
            navController = rememberNavController()
        )
    }
}
