package com.example.mobilesecurity.screens.groupchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GroupchatViewModel(private val repository: AccountRepository) : ViewModel() {
    val groupchat_message = MutableStateFlow("")

    fun getGroupchatMessage() { //todo: get message from realtime db function

    }

    fun writeGroupchatMessage(newGroupchatMessage: String) {
        groupchat_message.value = newGroupchatMessage //todo: write message to realtime db function
    }
}

class GroupchatViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupchatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupchatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}