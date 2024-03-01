package com.example.mobilesecurity.model

import android.content.ContentValues.TAG
import android.util.Log
import com.example.mobilesecurity.model.service.firebase.User
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AccountRepository {

    val db = Firebase.firestore

    val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()


    fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    suspend fun signIn(email: String, password: String): Pair<Boolean, String?> {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Pair(true, null)
        } catch (e: Exception) {
            if (e is FirebaseAuthException) {
                Pair(false, e.localizedMessage)
            }
            Pair(false, "Wrong Email or Password")
        }
    }

    suspend fun signUp(email: String, username: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        addUserData(currentUserId, username)
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }

    suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }


    //Add user information on firestore
    fun addUserData(id: String, name: String) {

        val user = hashMapOf(
            "userName" to "$name",
            "userRole" to "student"
        )

        //Add new user document
        db.collection("users")
            .document(id)
            .set(user)
    }



}