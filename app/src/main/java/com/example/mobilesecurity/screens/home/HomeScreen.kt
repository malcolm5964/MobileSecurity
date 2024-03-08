package com.example.mobilesecurity.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilesecurity.R
import com.example.mobilesecurity.screens.sign_in.SignInViewModel


@Composable
fun HomeScreen(viewModel : HomeScreenViewModel = viewModel(),  navController: NavController, modifier: Modifier = Modifier) {

    val teams = viewModel.teams
    val userID = viewModel.userID


    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        Column {
            Text(
                text = "Create Team"
            )
            //add team button
            IconButton(modifier = Modifier.padding(innerPadding),onClick = { navController.navigate("createTeam_screen") }) {
                Icon(Icons.Filled.Add, contentDescription = "Home")
            }

            LazyColumn(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(teams) {Team ->
                    if(Team.teamMembers.any { it.userId == "$userID" }){
                        Log.d("Check list", "${Team}")
                        TeamButton(teamID = Team.id, teamName = Team.teamName,navController = navController)
                   }

                }
            }
        }

    }
}

@Composable
fun TeamButton(teamID: String, teamName: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("groupchat_screen/$teamID/$teamName")
            }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "$teamName",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}




@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*navController.navigate("home_screen")*/ }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
            IconButton(onClick = { navController.navigate("search_screen") }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
            IconButton(onClick = { navController.navigate("profile_screen") }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
        }
    }
}