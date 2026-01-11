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
fun RegisterView(
    modifier: Modifier = Modifier,
    navController : NavController
){
    val viewModel : RegisterViewModel = viewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("REGISTER", modifier = Modifier.padding(16.dp))
        
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.name,
            onValueChange = {
                viewModel.onChangeName(it)
            },
            label = {
                Text("name")
            }
        )
        
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
        
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.confirmPassword,
            onValueChange = {
                viewModel.onChangeConfirmPassword(it)
            },
            label = {
                Text("confirm password")
            }
        )
        
        if (uiState.error != null) {
            Text(uiState.error!!, color = Color.Red)
        }
        
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                viewModel.register(onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                })
            },
            enabled = !uiState.loading
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(8.dp))
            } else {
                Text("Register")
            }
        }
        
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = { navController.navigate("login") }
        ) {
            Text("Back to Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    GameTheme {
        RegisterView(
            navController = rememberNavController()
        )
    }
}
