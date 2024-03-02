package com.example.mobilesecurity.screens.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.service.firebase.User
import com.example.mobilesecurity.screens.home.HomeScreenViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: AccountRepository) : ViewModel() {

    val user = mutableStateOf(User())
    val username = mutableStateOf("")

    init {
        viewModelScope.launch {
            val fetchedUser = repository.getUserData()
            user.value = fetchedUser
            username.value = fetchedUser.username
        }
    }

    fun updateUsername(newUsername: String) {
        username.value = newUsername
    }
}



class ProfileViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}