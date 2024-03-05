package com.example.mobilesecurity.screens.createTeam

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.model.SearchItem


@Composable
fun CreateTeamScreen(viewModel: CreateTeamViewModel = viewModel(), navController: NavController) {
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TextField for entering team name
            TextField(
                value = viewModel.teamName,
                onValueChange = { viewModel.teamName = it },
                label = { Text("Team Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            // Button for creating the team
            Button(
                onClick =
                {
                    viewModel.createTeam()
                    navController.navigate("home_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Create Team")
            }
            SearchScreen(
                searchQuery = viewModel.searchQuery,
                searchResults = searchResults,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                navController = navController,
                innerPadding = innerPadding,
                viewModel = viewModel
            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<SearchItem>,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController = rememberNavController(),
    innerPadding: PaddingValues,
    viewModel: CreateTeamViewModel = viewModel()
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(innerPadding)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = {},
            active = true,
            onActiveChange = {},
            placeholder =
            {
                Text(text = "Search Name or Group Channel")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            },
            content = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = searchResults.size,
                        key = { index -> searchResults[index].id },
                        itemContent = { index ->
                            val item = searchResults[index]
                            SearchListItem(searchItem = item, navController = navController, viewModel = viewModel)
                        }
                    )
                }
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            tonalElevation = 0.dp
        )
    }
}

@Composable
fun SearchListItem(
    modifier: Modifier = Modifier,
    searchItem: SearchItem,
    navController: NavController = rememberNavController(),
    viewModel: CreateTeamViewModel = viewModel()
) {
    var checked by remember {
        mutableStateOf(false)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable
            {
                /*TODO: Navigate to user profile page or group page*/
            }
    ) {
        Text(text = if (searchItem.type == "Group") "# " + searchItem.name else searchItem.name)
        Checkbox(checked = checked, onCheckedChange = {
            checked = it
            if (checked)
            {
                viewModel.addUserToSelected(searchItem.id)
            }
            else if (!checked) {
                viewModel.removeUserFromSelected(searchItem.id)
            }
        })
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