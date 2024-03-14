package com.example.mobilesecurity.screens.invitecontact

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.BottomNavigationBar
import com.example.mobilesecurity.model.Contact

@Composable
fun InviteContactScreen(viewModel: InviteContactViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    var context = LocalContext.current
    viewModel.getContacts(context)
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }) { innerPadding ->
        Column {
            SearchScreen(
                searchQuery = viewModel.searchQuery,
                searchResults = searchResults,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                innerPadding = innerPadding,
                context = context
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Contact>,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController = rememberNavController(),
    innerPadding: PaddingValues,
    context : Context
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
                Text(text = "Seach Contact")
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
                            SearchListItem(searchItem = item, navController = navController, context = context)
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
    searchItem: Contact,
    navController: NavController = rememberNavController(),
    context : Context
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable
            {
                /*TODO: Send message to contoct*/
                try {
                    val message = "Hey ${searchItem.name}, I am using this app (EduEngage) to connect with our teachers and friends. Please install it from the link: www.EduEngage.com"
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(searchItem.phone, null, message, null, null)
                    Toast.makeText(
                        context,
                        "Message Sent",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error : " + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    ) {
        Text(text = searchItem.name)
        Icon(
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}