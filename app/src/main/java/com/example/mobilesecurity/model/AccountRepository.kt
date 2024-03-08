package com.example.mobilesecurity.model

import android.util.Log
import com.example.mobilesecurity.model.service.firebase.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID

class AccountRepository {

    val db = Firebase.firestore
    data class TeamMessage(val userID: String, val content: String, val timestamp: Timestamp)

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

    suspend fun signUp(email: String, username: String, password: String, role: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        addUserData(currentUserId, username, role)
    }

    //error in this method
    suspend fun createTeam(teamName: String, teamMember: String) {
        // Convert teamMembers (List<User>) into a list of user IDs (List<String>)
      //  val teamMember = teamMembers.map { it.id }

        // Now call addTeamData with teamName and the list of member IDs
        //addTeamData(teamName, teamMember)
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }

    suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }


    //Add user information on firestore
    fun addUserData(id: String, name: String, role: String) {

        val user = hashMapOf(
            "userName" to name,
            "userRole" to role
        )

        //Add new user document
        db.collection("users")
            .document(id)
            .set(user)
    }

    //create new team, on firestore
    fun addTeamData(team: Team){
        db.collection("teams")
            .document(team.id)
            .set(team)
    }

    fun addTeamMember(team: Team, userId: String){
        db.collection("teams")
            .document(team.id)
            .update("teamMembers", team.teamMembers + TeamUsers(false, userId))
    }

    suspend fun getUserData(): User {
        return try {
            Log.d("AccountRepository", "Fetching user data for $currentUserId")
            val documentSnapshot = db.collection("users").document(currentUserId).get().await()
            User(documentSnapshot.id, documentSnapshot.getString("userName") ?: "", documentSnapshot.getString("userRole") ?: "") // Return a default user if data is null
            //documentSnapshot.toObject<User>() ?: User() // Return a default user if data is null
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching user data", e)
            User() // Return a default user in case of an exception
        }
    }

    suspend fun getUserData(userId: String = ""): User {
        return try {
            Log.d("AccountRepository", "Fetching user data for $userId")
            val documentSnapshot = db.collection("users").document(userId).get().await()
            User(documentSnapshot.id, documentSnapshot.getString("userName") ?: "", documentSnapshot.getString("userRole") ?: "") // Return a default user if data is null
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching user data", e)
            User() // Return a default user in case of an exception
        }
    }

    // todo: function to get message from realtime db
//    suspend fun getGroupchatMessageData(): List<GroupchatMessage> {
//        return try {
//            val messageSnapshot = db.collection("teamMessages").document().get().await()
//            val groupchatMessageList = mutableListOf<GroupchatMessage>()
//            for (document in messageSnapshot.documents) {
//                val userID = document.getString("userID") ?: ""
//                val content = document.getString("content") ?: ""
//                val timestamp = document.getTimestamp("timestamp") // Assuming 'timestamp' is a Firestore Timestamp
//
//                val groupchatMessage = GroupchatMessage(userID, content, timestamp)
//                groupchatMessageList.add(groupchatMessage)
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }

    suspend fun updateUsername(newUsername: String): Pair<Boolean, String> {
        val userUpdate: Map<String, Any> = mapOf("userName" to newUsername)
        return try {
            db.collection("users").document(currentUserId).update(userUpdate).await()
            Pair(true, "Username updated successfully.")
        } catch (e: Exception) {
            Pair(false, "Error updating user username: ${e.message}")
        }
    }

    suspend fun updatePassword(newPassword: String): Pair<Boolean, String> {
        return try {
            FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)?.await()
            Pair(true, "Password updated successfully.")
        } catch (e: Exception) {
            Pair(false, "Error updating password: ${e.message}")
        }
    }

}
