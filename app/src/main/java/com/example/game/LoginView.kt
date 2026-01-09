package com.example.game.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.game.ui.theme.GameTheme

@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    navController : NavController
){
    val viewModel : LoginViewModel = viewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("LOGIN", modifier = Modifier.padding(16.dp))
        
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.email,
            onValueChange = {
                viewModel.onChangeEmail(it)
            },
            label = {
                Text("email")
            }
        )
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.password,
            onValueChange = {
                viewModel.onChangePassword(it)
            },
            label = {
                Text("password")
            }
        )
        if (uiState.error != null) {
            Text(uiState.error!!, color = Color.Red)
        }
        
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                viewModel.login(onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            },
            enabled = !uiState.loading
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(8.dp))
            } else {
                Text("Login")
            }
        }
        
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = { navController.navigate("register") }
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    GameTheme {
        LoginView(
            navController = rememberNavController()
        )
    }
}
