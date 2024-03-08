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

val updateMessage = MutableStateFlow<Pair<Boolean, String>?>(null)
class ProfileViewModel(private val repository: AccountRepository) : ViewModel() {

    val user = mutableStateOf(User())
    val username = mutableStateOf("")
    val password = mutableStateOf("")

    init {
        viewModelScope.launch {

            val fetchedUser = repository.getUserData()
            user.value = fetchedUser
            username.value = fetchedUser.username
            Log.d("ProfileViewModel", "User: $fetchedUser")
        }
    }

    fun updateUserInfo(newUsername: String, newPassword: String) {
        viewModelScope.launch {
            var updateOccurred = false
            var message = ""

            if (newUsername.isNotEmpty()) {
                val (success, updateMessage) = repository.updateUsername(newUsername)
                if (success) {
                    user.value = user.value.copy(username = newUsername)
                    username.value = newUsername
                    updateOccurred = true
                }
                message += updateMessage // Append message regardless of success to provide feedback
            }

            if (newPassword.isNotEmpty()) {
                val (success, updateMessage) = repository.updatePassword(newPassword)
                if (success) {
                    password.value = newPassword
                    updateOccurred = true
                } else {
                    // If password update fails, consider not clearing fields to allow user correction
                }
                if (message.isNotEmpty()) message += "\n" // Add a newline if there was a previous message
                message += updateMessage
            }

            if (updateOccurred) {
                // Clear the fields after update
                username.value = ""
                password.value = ""
                Log.d("ProfileViewModel", "Update successful: $message")
            } else {
                Log.d("ProfileViewModel", "Update not performed: $message")
            }
        }
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