package com.example.mobilesecurity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick =
            {
                if (navController.currentDestination?.route != "home_screen")
                    navController.navigate("home_screen")
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
            IconButton(onClick =
            {
                if (navController.currentDestination?.route != "search_screen")
                    navController.navigate("search_screen")
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick =
            {
                if (navController.currentDestination?.route != "profile_screen")
                    navController.navigate("profile_screen")
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
            IconButton(onClick =
            {
                if (navController.currentDestination?.route != "invite_contact_screen")
                    navController.navigate("invite_contact_screen")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Invite Contact")
            }
        }
    }
}