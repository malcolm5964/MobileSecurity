package com.example.mobilesecurity.screens.view_profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.model.Team
import com.example.mobilesecurity.model.service.firebase.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewProfileViewModel(private val repository: AccountRepository, private val userId: String, private val searchRepository: SearchRepository) : ViewModel() {

    private val selectedUser = mutableStateOf(User())
    val selectedUsername = mutableStateOf("")

    val currentUser = mutableStateOf(User())
    val currentUserName = mutableStateOf("")

    private val _teamItems = MutableStateFlow<List<Team>>(emptyList())
    private val teamItems: StateFlow<List<Team>> = _teamItems

    var searchQuery by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            Log.d("ViewProfileViewModel", "userId: $userId")
            val fetchedUser = repository.getUserData(userId)
            selectedUser.value = fetchedUser
            selectedUsername.value = fetchedUser.username
            Log.d("ViewProfileViewModel", "User: $fetchedUser")

            val fetchedCurrentUser = repository.getUserData()
            currentUser.value = fetchedCurrentUser
            currentUserName.value = fetchedCurrentUser.username

            val teams = searchRepository.getAllTeams()
            // check if user is in team and if user is admin
            val filteredTeams = teams.filter { team ->
                val currentUserAdmin = team.teamMembers.any { it.userId == currentUser.value.id && it.admin }
                val selectedUserNotInTeam = team.teamMembers.none { it.userId == selectedUser.value.id }
                currentUserAdmin && selectedUserNotInTeam
            }
            filteredTeams.forEach { team ->
                Log.d("ViewProfileViewModel", "Team: ${team.teamName}")
                team.teamMembers.forEach { user ->
                    Log.d("ViewProfileViewModel", "User: ${user.userId}")
                }
            }
            _teamItems.value = filteredTeams
        }
    }

    fun onSearchQueryChange(searchQuery: String) {
        this.searchQuery = searchQuery
    }

    val searchResults: StateFlow<List<Team>> =
        snapshotFlow { searchQuery }
            .combine(teamItems) { searchQuery, items ->
                when {
                    searchQuery.isNotEmpty() -> items.filter { item ->
                        item.teamName.contains(searchQuery, ignoreCase = true)
                    }
                    else -> items
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun addTeamMember(team: Team) {
        repository.addTeamMember(team, selectedUser.value.id)
        viewModelScope.launch {
            // Refresh the list of teams and update searchResults
            val teams = searchRepository.getAllTeams()
            val filteredTeams = teams.filter { updatedTeam ->
                val currentUserAdmin = updatedTeam.teamMembers.any { it.userId == currentUser.value.id && it.admin }
                val selectedUserNotInTeam = updatedTeam.teamMembers.none { it.userId == selectedUser.value.id }
                currentUserAdmin && selectedUserNotInTeam
            }
            _teamItems.value = filteredTeams
        }
    }
}

class ViewProfileViewModelFactory(private val repository: AccountRepository, private val userId: String, private val searchRepository: SearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewProfileViewModel(repository, userId, searchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}