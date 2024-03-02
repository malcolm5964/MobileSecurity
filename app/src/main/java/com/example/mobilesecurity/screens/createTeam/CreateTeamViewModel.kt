package com.example.mobilesecurity.screens.createTeam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilesecurity.model.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CreateTeamViewModel(private val repository: AccountRepository) : ViewModel() {
    val userID = repository.currentUserId
    val teamName = MutableStateFlow("")
    val teamMember = MutableStateFlow("")
    val userName = MutableStateFlow("")
    fun createTeamClick(navController: NavController) = viewModelScope.launch {
        //the repo is wrong
        repository.createTeam(teamName.value, teamMember.value )
        navController.navigate("home_screen")
    }




}

class CreateTeamViewModelFactory(private val repository: AccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTeamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateTeamViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}