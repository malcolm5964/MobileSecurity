package com.example.mobilesecurity.screens.profile


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.mobilesecurity.screens.home.BottomNavigationBar


@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }

}