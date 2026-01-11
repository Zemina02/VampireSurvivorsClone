package com.example.game.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.game.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    topBarTitle: String,
    isHomeScreen: Boolean,
    navController: NavController
) {
    TopAppBar(
        title = { Text(topBarTitle) },
        actions = {

        },
        navigationIcon = {
            if (!isHomeScreen) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.caixas),
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}
