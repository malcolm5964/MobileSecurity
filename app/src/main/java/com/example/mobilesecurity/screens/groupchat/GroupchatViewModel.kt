package com.example.mobilesecurity.screens.groupchat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.Message
import com.example.mobilesecurity.model.MessageRepository
import com.example.mobilesecurity.model.Team
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class GroupchatViewModel(private val accountRepository: AccountRepository, private val messageRepository: MessageRepository) : ViewModel() {

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