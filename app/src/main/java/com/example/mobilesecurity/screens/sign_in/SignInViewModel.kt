package com.example.mobilesecurity.screens.sign_in

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilesecurity.model.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AccountRepository) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(navController: NavController, context: Context) = viewModelScope.launch{
        val (signInSuccessful, errorMessage) = repository.signIn(email.value, password.value)
        if (signInSuccessful) {
            navController.navigate("home_screen")
        } else {
            errorMessage?.let {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

class SignInViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}