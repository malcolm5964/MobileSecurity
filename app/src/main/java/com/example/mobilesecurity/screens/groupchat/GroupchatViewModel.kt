package com.example.mobilesecurity.screens.groupchat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.Message
import com.example.mobilesecurity.model.MessageRepository
import com.example.mobilesecurity.model.Team
import com.example.mobilesecurity.model.UserLocationWithAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import java.util.Date


class GroupchatViewModel(private val accountRepository: AccountRepository, private val messageRepository: MessageRepository) : ViewModel() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Location coordinates in a mutable state
    private val _location = MutableStateFlow(Pair(0.0, 0.0))
    val location: StateFlow<Pair<Double, Double>> = _location
    lateinit var geoCoder: Geocoder

    val groupChatMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val userID = accountRepository.currentUserId
    val username = mutableStateOf("")

    init {
        viewModelScope.launch {
            username.value = accountRepository.getUserData(accountRepository.currentUserId).username
        }
    }
    val currentUserId = accountRepository.currentUserId
    val teamId = mutableStateOf("")
    val isCurrentUserInsideTeam = mutableStateOf(false)

    private val _team = MutableStateFlow(Team())
    val team: StateFlow<Team> = _team

    fun getTeam(teamId: String) {
        viewModelScope.launch {
            val fetchedTeam = accountRepository.getTeamData(teamId)
            //check if current user is inside team

//            val teamMemberIds = emptyList<String>()
//            val teamMembers = fetchedTeam.teamMembers
//            teamMembers.forEach { teamMember ->
//                teamMemberIds + teamMember.userId
//            }
            isCurrentUserInsideTeam.value = fetchedTeam.toString().contains(currentUserId)
            _team.value = fetchedTeam
        }
    }

    fun addTeamMember() {
        accountRepository.addTeamMember(_team.value, currentUserId)
        //refresh team
        getTeam(_team.value.id)
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = snapshot.getValue(Message::class.java)
            message?.let { newMessage ->
                groupChatMessages.value = groupChatMessages.value + listOf(newMessage)
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    fun getGroupChatMessages(groupChatID: String) {
        messageRepository.getGroupChatMessages(groupChatID) { messages ->
            groupChatMessages.value = messages
        }
        messageRepository.addChildEventListener(groupChatID, childEventListener)
    }

    fun removeChildEventListener(groupChatID: String) {
        messageRepository.removeChildEventListener(groupChatID, childEventListener)
    }

    fun addMessage(message: String, groupChatID: String) {
        val timestamp = Date()
        val message = Message(accountRepository.currentUserId, username.value, message, timestamp)
        messageRepository.sendMessage(message, groupChatID)
    }

    fun initializeMapVariables(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        geoCoder = Geocoder(context)
    }

    /**
     * Retrieves the current user location asynchronously.
     *
     * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
     *        It provides a Pair representing latitude and longitude.
     * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
     *        It provides the Exception that occurred.
     * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
     *        If set to false, it uses balanced power accuracy.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetCurrentLocationFailed: (Exception) -> Unit,
        priority: Boolean = true,
        context: Context
    ) {
        // Determine the accuracy priority based on the 'priority' parameter
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        // Check if location permissions are granted
        if (areLocationPermissionsGranted(context)) {
            // Retrieve the current location asynchronously
            fusedLocationProviderClient.getCurrentLocation(
                accuracy, CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                location?.let {
                    // If location is not null, invoke the success callback with latitude and longitude
                    _location.value = Pair(it.latitude, it.longitude)
                    val address = geoCoder.getFromLocation(it.latitude, it.longitude, 1)
                    val text = address?.get(0)?.getAddressLine(0).toString()
                    val addressQuery = "https://www.google.com/maps/search/?api=1&query=${text.replace(" ", "+")}"
                    viewModelScope.launch {
                        accountRepository.saveUserLocationWithAddress(currentUserId, text, it.latitude, it.longitude, Date())
                    }
                    Log.d("LocationAddressWithCoordinates", "Address: $addressQuery, Coordinates: ${it.latitude}, ${it.longitude}")
                    onGetCurrentLocationSuccess(_location.value)
                }
            }.addOnFailureListener { exception ->
                // If an error occurs, invoke the failure callback with the exception
                onGetCurrentLocationFailed(exception)
            }
        }
    }

    /**
     * Checks if location permissions are granted.
     *
     * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
     */
    private fun areLocationPermissionsGranted(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
}

class GroupchatViewModelFactory(private val accountRepository: AccountRepository, private val messageRepository: MessageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupchatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupchatViewModel(accountRepository, messageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}