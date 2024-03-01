package com.example.mobilesecurity.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.screens.sign_in.SignInViewModel


class makeadminViewModel(private val repository: AccountRepository) : ViewModel() {

    val userID = repository.currentUserId


}

class makeadminViewModelFactory(private val repository: AccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(makeadminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return makeadminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}