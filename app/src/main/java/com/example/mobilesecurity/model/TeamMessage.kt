package com.example.mobilesecurity.model

import com.google.firebase.Timestamp

data class TeamMessage(
    val userID: String,
    val content: String,
    val timestamp: Timestamp
)