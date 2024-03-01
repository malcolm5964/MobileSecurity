package com.example.mobilesecurity.screens.sign_up

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.service.firebase.User
import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: AccountRepository) : ViewModel() {

    val email = MutableStateFlow("")
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val role = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updateUsername(newUsername: String) {
        username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(navController: NavController) = viewModelScope.launch {
        repository.signUp(email.value, username.value, password.value )
        navController.navigate("home_screen")
    }
}

class SignUpViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}