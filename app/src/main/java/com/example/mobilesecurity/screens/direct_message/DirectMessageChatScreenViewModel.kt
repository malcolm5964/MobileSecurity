package com.example.mobilesecurity.screens.direct_message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.screens.search.SearchScreenViewModel

class DirectMessageChatScreenViewModel(private val repository: AccountRepository) : ViewModel() {
    val userID = repository.currentUserId
}

class DirectMessageChatScreenViewModelFactory(private val repository: AccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectMessageChatScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirectMessageChatScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}