package com.example.mobilesecurity.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.model.SearchItem
import com.example.mobilesecurity.BottomNavigationBar

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: SearchScreenViewModel = viewModel(), navController: NavController = rememberNavController()) {
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        SearchScreen(
            searchQuery = viewModel.searchQuery,
            searchResults = searchResults,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<SearchItem>,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController = rememberNavController(),
    innerPadding: PaddingValues
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
                            SearchListItem(searchItem = item, navController = navController)
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
    navController: NavController = rememberNavController()
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable
            {
                /*TODO: Navigate to user profile page or group page*/
                if (searchItem.type == "Group") {
                    // navController.navigate("group/${searchItem.id}")
                    navController.navigate("groupchat_screen/${searchItem.id}/${searchItem.name}")
                } else {
                    // navController.navigate("user/${searchItem.id}")
                    val userId = searchItem.id
                    navController.navigate("profile_screen/$userId")
                }
            }
    ) {
        Text(text = if (searchItem.type == "Group") "# " + searchItem.name else searchItem.name)
        Icon(
            imageVector = Icons.Default.ArrowForward,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}