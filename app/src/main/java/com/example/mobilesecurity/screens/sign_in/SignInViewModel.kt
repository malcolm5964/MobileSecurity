package com.example.mobilesecurity.screens.sign_in

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilesecurity.model.AccountRepository
import com.google.firebase.firestore.FirebaseFirestore
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
            verifycredential(email.value,password.value)
            navController.navigate("home_screen")
        } else {
            errorMessage?.let {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

fun verifycredential(userEmail: String, userPassword: String) {
    val db = FirebaseFirestore.getInstance()

    // Check if the email already exists in the collection
    db.collection("password")
        .whereEqualTo("userEmail", userEmail)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Email doesn't exist, add the new entry
                db.collection("password")
                    .add(mapOf(
                        "userEmail" to userEmail,
                        "userPassword" to userPassword
                    ))
                    .addOnSuccessListener { documentReference ->
                        println("DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding document: $e")
                    }
            } else {
                // Email already exists, handle accordingly (e.g., display an error message)
                println("Email already exists in the collection.")
            }
        }
        .addOnFailureListener { e ->
            println("Error checking for existing email: $e")
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