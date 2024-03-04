package com.example.mobilesecurity.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchItem
import com.example.mobilesecurity.model.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchScreenViewModel(private val repository: AccountRepository, private val searchRepository: SearchRepository) : ViewModel() {
    val userID = repository.currentUserId
    var searchQuery by mutableStateOf("")
        private set

    private val _searchItems = MutableStateFlow<List<SearchItem>>(emptyList())
    val searchItems: StateFlow<List<SearchItem>> = _searchItems

    init {
        viewModelScope.launch {
            val users = searchRepository.getAllUsers()
            val userSearchItem = users.map { user ->
                SearchItem(
                    id = user.id,
                    name = user.username,
                    type = "User"
                )
            }
            for (user in users) {
                Log.d("SearchScreenViewModel - users", user.toString())
            }
            addUserSearchItems(userSearchItem)

            val teams = searchRepository.getAllTeams()
            for (team in teams) {
                Log.d("SearchScreenViewModel - teams", team.toString())
            }

            val teamSearchItem = teams.map { team ->
                SearchItem(
                    id = team.id,
                    name = team.name,
                    type = "Group"
                )
            }

            addTeamSearchItems(teamSearchItem)
        }
    }

    private fun addUserSearchItems(userSearchItems: List<SearchItem>) {
        _searchItems.value += userSearchItems
    }

    private fun addTeamSearchItems(teamSearchItems: List<SearchItem>) {
        _searchItems.value += teamSearchItems
    }


    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    val searchResults: StateFlow<List<SearchItem>> =
        snapshotFlow { searchQuery }
            .combine(searchItems) { searchQuery, items ->
                when {
                    searchQuery.isNotEmpty() -> items.filter { item ->
                        item.name.contains(searchQuery, ignoreCase = true)
                    }
                    else -> items
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )
}

class SearchScreenViewModelFactory(private val repository: AccountRepository, private val searchRepository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchScreenViewModel(repository, searchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

private val searchItemFlow = flowOf(
    listOf(
        SearchItem(
            id = "1",
            name = "John",
            type = "User"
        ),
        SearchItem(
            id = "2",
            name = "Mary",
            type = "User"
        ),
        SearchItem(
            id = "3",
            name = "ICT2112",
            type = "Group"
        ),
        SearchItem(
            id = "4",
            name = "INF2007",
            type = "Group"
        ),
        SearchItem(
            id = "5",
            name = "ICT2215",
            type = "Group"
        ),
        SearchItem(
            id = "6",
            name = "ICT2113",
            type = "Group"
        ),
    )
)