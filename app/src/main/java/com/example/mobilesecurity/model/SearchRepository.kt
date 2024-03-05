package com.example.mobilesecurity.model

import com.example.mobilesecurity.model.service.firebase.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class SearchRepository {

    val db = Firebase.firestore

    suspend fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val querySnapshot = db.collection("users").get().await()
        for (document in querySnapshot.documents) {
            val user = User().apply {
                id = document.id
                username = document.getString("userName") ?: ""
                role = document.getString("userRole") ?: ""
            }
            users.add(user)
        }
        return users
    }

    suspend fun getAllTeams(): List<Team> {
        val teams = mutableListOf<Team>()
        val querySnapshot = db.collection("teams").get().await()
        for (document in querySnapshot.documents) {
            val team = Team().apply {
                id = document.id
                teamName = document.getString("teamName") ?: ""
                teamMembers = document.get("teamMembers") as List<TeamUsers>
            }
            teams.add(team)
        }
        return teams
    }
}