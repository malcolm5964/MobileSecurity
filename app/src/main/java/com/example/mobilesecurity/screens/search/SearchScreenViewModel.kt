package com.example.mobilesecurity.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class SearchScreenViewModel(private val repository: AccountRepository) : ViewModel() {
    val userID = repository.currentUserId
    var searchQuery by mutableStateOf("")
        private set
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    val searchResults: StateFlow<List<SearchItem>> =
        snapshotFlow { searchQuery }
            .combine(searchItemFlow) { searchQuery, movies ->
                when {
                    searchQuery.isNotEmpty() -> movies.filter { movie ->
                        movie.name.contains(searchQuery, ignoreCase = true)
                    }
                    else -> movies
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )
}

class SearchScreenViewModelFactory(private val repository: AccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class SearchItem(
    val id: Long,
    val name: String,
    val type: String
)

private val searchItemFlow = flowOf(
    listOf(
        SearchItem(
            id = 1,
            name = "John",
            type = "User"
        ),
        SearchItem(
            id = 2,
            name = "Mary",
            type = "User"
        ),
        SearchItem(
            id = 3,
            name = "ICT2112",
            type = "Group"
        ),
        SearchItem(
            id = 4,
            name = "INF2007",
            type = "Group"
        ),
        SearchItem(
            id = 5,
            name = "ICT2215",
            type = "Group"
        ),
        SearchItem(
            id = 6,
            name = "ICT2113",
            type = "Group"
        ),
    )
)