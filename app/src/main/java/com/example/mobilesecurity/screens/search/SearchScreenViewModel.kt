package com.example.mobilesecurity.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilesecurity.model.AccountRepository

class SearchScreenViewModel(private val repository: AccountRepository) : ViewModel() {
    val userID = repository.currentUserId
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