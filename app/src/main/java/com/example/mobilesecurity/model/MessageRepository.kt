package com.example.mobilesecurity.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MessageRepository {

    val database = Firebase.database("https://mobilesecurity-e692e-default-rtdb.asia-southeast1.firebasedatabase.app/")


    fun getGroupChatMessages(groupChatID: String, callback: (List<Message>) -> Unit) {
        val myRef = database.getReference("messages").child(groupChatID)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { messageSnapshot ->
                    messageSnapshot.getValue(Message::class.java)
                }
                callback(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun addChildEventListener(groupChatID: String, childEventListener: ChildEventListener) {
        val myRef = database.getReference("messages").child(groupChatID)
        myRef.addChildEventListener(childEventListener)
    }

    fun removeChildEventListener(groupChatID: String, childEventListener: ChildEventListener) {
        val myRef = database.getReference("messages").child(groupChatID)
        myRef.removeEventListener(childEventListener)
    }

    fun sendMessage(message: Message, groupChatID: String) {
        val myRef = database.getReference("messages").child(groupChatID).push()
        myRef.setValue(message)
    }

}