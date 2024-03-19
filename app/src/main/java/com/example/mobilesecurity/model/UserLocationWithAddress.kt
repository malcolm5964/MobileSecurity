package com.example.mobilesecurity.model

import java.util.Date

data class UserLocationWithAddress(
    val userId: String = "",
    val locations: List<LocationWithAddress> = emptyList()
)

data class LocationWithAddress(
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Date = Date()
)