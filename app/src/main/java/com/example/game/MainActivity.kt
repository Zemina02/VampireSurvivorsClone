package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.game.game.GameOverView
import com.example.game.game.GameScreenView
import com.example.game.game.GameViewModel
import com.example.game.highscore.HighscoreView
import com.example.game.highscore.HighscoreViewModel
import com.example.game.login.LoginView
import com.example.game.login.RegisterView
import com.example.game.profile.ProfileView
import com.example.game.ui.theme.GameTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var highscoreViewModel: HighscoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        highscoreViewModel = ViewModelProvider(this).get(HighscoreViewModel::class.java)

        setContent {
            GameTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    val startDestination = if (Firebase.auth.currentUser != null) "home" else "login"

                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                    ) {
                        // Auth
                        composable("login") {
                            LoginView(navController = navController)
                        }

                        composable("register") {
                            RegisterView(navController = navController)
                        }

                        // Main Menu
                        composable("home") {
                            HomeView(navController = navController)
                        }

                        // Profile
                        composable("profile") {
                            ProfileView(navController = navController)
                        }

                        // Highscore
                        composable("highscore") {
                            val userId = Firebase.auth.currentUser?.uid ?: ""
                            HighscoreView(
                                viewModel = highscoreViewModel,
                                userId = userId,
                                navController = navController
                            )
                        }

                        // Game
                        composable("game") {
                            LaunchedEffect(Unit) {
                                gameViewModel.resetGame()
                            }

                            GameScreenView(
                                navController = navController,
                                viewModel = gameViewModel
                            )
                        }


                        // Game Over
                        composable(
                            "gameOver/{score}",
                            arguments = listOf(
                                navArgument("score") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val score = backStackEntry.arguments?.getInt("score") ?: 0
                            GameOverView(
                                finalScore = score,
                                navController = navController,
                                viewModel = gameViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
