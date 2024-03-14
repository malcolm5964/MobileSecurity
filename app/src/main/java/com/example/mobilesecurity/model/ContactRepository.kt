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

    fun addContact(contact: Contact) {
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
                            println("Contact added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding contact: $e")
                        }
                } else {
                    println("Contact with phone number ${contact.phone} already exists.")
                }
            }
            .addOnFailureListener { e ->
                println("Error checking for existing contact: $e")
            }
    }

}