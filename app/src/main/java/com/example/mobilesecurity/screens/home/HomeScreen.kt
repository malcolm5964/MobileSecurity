package com.example.mobilesecurity.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilesecurity.BottomNavigationBar
import com.example.mobilesecurity.R
import com.example.mobilesecurity.screens.sign_in.SignInViewModel


@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    val teams = viewModel.teams
    val userID = viewModel.userID
    // State to control the expansion of the Teams dropdown, defaulting to expanded
    var teamsExpanded by remember { mutableStateOf(true) } // Set to true for default expanded state

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Header for Teams dropdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { teamsExpanded = !teamsExpanded } // Toggle expansion on click
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Teams",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (teamsExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (teamsExpanded) "Collapse" else "Expand"
                )
            }
            // Teams list that shows when expanded
            if (teamsExpanded) {
                LazyColumn(modifier = Modifier.height(640.dp)) {
                    items(teams) { team ->
                        if (team.teamMembers.any { it.userId == userID }) {
                            TeamButton(teamID = team.id, teamName = team.teamName, navController = navController)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // This pushes the button to the bottom of the screen
            // Create Team button at the bottom of the screen
            Button(
                onClick = { navController.navigate("createTeam_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Create Team")
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