package com.example.mobilesecurity.model

data class Team(
    var id: String = "",
    var name: String = "",
    var members: List<TeamUsers> = emptyList()
)

data class TeamUsers(
    val isAdmin: Boolean = false,
    val userId: String = ""
)
