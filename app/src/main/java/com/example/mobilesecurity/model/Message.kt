package com.example.mobilesecurity.model

import java.util.Date

data class Message(
    val userID: String? = "",
    val username: String? = "",
    val content: String? = "",
    val timestamp: Date? = Date()
)