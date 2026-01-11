package com.example.game.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.game.components.MyTopBar
import com.example.game.ui.theme.GameTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: ProfileViewModel = viewModel()
    val uiState by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Scaffold(
        topBar = {
            MyTopBar(
                topBarTitle = "Profile",
                isHomeScreen = false,
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
            TextField(
                modifier = Modifier.padding(8.dp),
                value = uiState.name,
                onValueChange = { viewModel.onChangeName(it) },
                label = { Text("name") }
            )
            TextField(
                modifier = Modifier.padding(8.dp),
                value = uiState.email,
                onValueChange = { viewModel.onChangeEmail(it) },
                label = { Text("email") }
            )
            TextField(
                modifier = Modifier.padding(8.dp),
                value = uiState.bio,
                onValueChange = { viewModel.onChangeBio(it) },
                label = { Text("bio") }
            )
            if (uiState.hasChange) {
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = { viewModel.saveChanges() }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    GameTheme {
        ProfileView(
            navController = rememberNavController()
        )
    }
}
