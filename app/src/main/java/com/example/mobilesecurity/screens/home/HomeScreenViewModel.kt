package com.example.mobilesecurity.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.model.Team
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val accountRepository: AccountRepository, private val searchRepository: SearchRepository) : ViewModel() {

    var teams = mutableListOf<Team>()
    var userID = accountRepository.currentUserId
    init {
        viewModelScope.launch {
            teams = searchRepository.getAllTeams().toMutableList()
        }
    }




}

class HomeScreenViewModelFactory(private val accountRepository: AccountRepository, private val searchRepository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(accountRepository, searchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}