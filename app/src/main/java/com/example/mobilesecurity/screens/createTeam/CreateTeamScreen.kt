package com.example.mobilesecurity.screens.createTeam

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController



@Composable
fun CreateTeamScreen(viewModel: CreateTeamViewModel = viewModel(), navController: NavController) {
    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            var teamName by remember { mutableStateOf("") }
            var searchQuery by remember { mutableStateOf("") }

            TextField(
                value = teamName,
                onValueChange = {
                    teamName = it
                    viewModel.teamName.value = it
                },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                  //  viewModel.searchUsers(it)
                },
                label = { Text("Search Members") },
                modifier = Modifier.fillMaxWidth()
            )

            //val searchResults by viewModel.searchResults.collectAsState()

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
              //  items(searchResults) { user ->
                //    Text(user.userName, modifier = Modifier.padding(8.dp).clickable {
               //         viewModel.addUserToGroup(user)
                  //  })
                    // Add UI to indicate selection, if needed
                }
            }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { viewModel.createTeamClick(navController) }
            ) {
                Text("Create Team")
            }
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
            IconButton(onClick = { navController.navigate("home_screen") }) {
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