package com.example.mobilesecurity.screens.view_profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.R
import com.example.mobilesecurity.BottomNavigationBar
import com.example.mobilesecurity.model.SearchItem
import com.example.mobilesecurity.model.Team
import com.example.mobilesecurity.ui.theme.Purple40

@Composable
fun ViewProfileScreen(viewModel: ViewProfileViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.photo_2024_03_08_15_21_18),
                    contentDescription = "Auth image",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp)
                        .width(300.dp)
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Username Field
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Username", modifier = Modifier.padding(start = 16.dp, end = 16.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(width = 2.dp, color = Purple40),
                                shape = RoundedCornerShape(50)
                            ),
                        singleLine = true,
                        value = viewModel.selectedUsername.value,
                        onValueChange = { viewModel.selectedUsername.value = it },
                        enabled = false
                    )
                }

                SearchScreen(
                    searchQuery = viewModel.searchQuery,
                    searchResults = searchResults,
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                    navController = navController,
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Team>,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController = rememberNavController(),
    innerPadding: PaddingValues,
    viewModel: ViewProfileViewModel = viewModel(),
    context: Context
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
                Text(text = "Search Team Name")
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
                            SearchListItem(searchItem = item, navController = navController, viewModel = viewModel, context = context)
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
    searchItem: Team,
    navController: NavController = rememberNavController(),
    viewModel: ViewProfileViewModel = viewModel(),
    context: Context
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth().clickable
        {
            viewModel.addTeamMember(searchItem)
            val msg = "Successfully added ${viewModel.selectedUsername.value} to ${searchItem.teamName}"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            Log.d("ViewProfileViewModel", msg)
        }
    ) {
        Text(text = searchItem.teamName)
        Icon(
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}