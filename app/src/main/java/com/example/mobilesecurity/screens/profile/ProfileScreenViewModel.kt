package com.example.mobilesecurity.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.screens.home.HomeScreenViewModel
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(private val repository: AccountRepository) : ViewModel() {


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