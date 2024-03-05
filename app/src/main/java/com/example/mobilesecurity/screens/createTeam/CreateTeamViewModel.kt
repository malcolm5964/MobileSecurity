package com.example.mobilesecurity.screens.createTeam

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchItem
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.model.Team
import com.example.mobilesecurity.model.TeamUsers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID


class CreateTeamViewModel(private val repository: AccountRepository, private val searchRepository: SearchRepository) : ViewModel(){
    val userID = repository.currentUserId
    var searchQuery by mutableStateOf("")
        private set

    var teamName by mutableStateOf("")

    private val _searchItems = MutableStateFlow<List<SearchItem>>(emptyList())
    private val searchItems: StateFlow<List<SearchItem>> = _searchItems

    private var selectedUsers: MutableList<String> = mutableListOf()

    // Function to add a user to the selected users list
    fun addUserToSelected(user: String) {
        if (!selectedUsers.contains(user)) {
            selectedUsers.add(user)
            Log.d("CreateTeamViewModel", selectedUsers.toString())
        }
    }

    // Function to remove a user from the selected users list
    fun removeUserFromSelected(user: String) {
        selectedUsers.remove(user)
        Log.d("CreateTeamViewModel", selectedUsers.toString())
    }

    fun createTeam() {
        var team = Team(teamName = teamName, id = UUID.randomUUID().toString())
        selectedUsers.add(userID)
        team.teamMembers = selectedUsers.map { userIdInList ->
            TeamUsers(
                isAdmin = userIdInList == userID,
                userId = userIdInList
            )
        }
        Log.d("createTeam", team.toString())
        repository.addTeamData(team)
    }

    init {
        viewModelScope.launch {
            val users = searchRepository.getAllUsers()
            val otherUsers = users.filter { user ->
                user.id != userID
            }
            val userSearchItem = otherUsers.map { user ->
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

class CreateTeamViewModelFactory(private val repository: AccountRepository, private val searchRepository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTeamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateTeamViewModel(repository, searchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}