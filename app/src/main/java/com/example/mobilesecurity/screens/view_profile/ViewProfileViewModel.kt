package com.example.mobilesecurity.screens.view_profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.service.firebase.User
import kotlinx.coroutines.launch

class ViewProfileViewModel(private val repository: AccountRepository, private val userId: String) : ViewModel() {

    private val selectedUser = mutableStateOf(User())
    val selectedUsername = mutableStateOf("")

    init {
        viewModelScope.launch {
            Log.d("ViewProfileViewModel", "userId: $userId")
            val fetchedUser = repository.getUserData(userId)
            selectedUser.value = fetchedUser
            selectedUsername.value = fetchedUser.username
            Log.d("ViewProfileViewModel", "User: $fetchedUser")
        }
    }
}

class ViewProfileViewModelFactory(private val repository: AccountRepository, private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewProfileViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}