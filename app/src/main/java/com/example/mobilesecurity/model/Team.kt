package com.example.mobilesecurity.model

data class Team(
    var id: String = "",
    var teamName: String = "",
    var teamMembers: List<TeamUsers> = emptyList()
)

data class TeamUsers(
    val isAdmin: Boolean = false,
    val userId: String = ""
)
