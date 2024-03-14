package com.example.mobilesecurity.screens.invitecontact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.Contact
import com.example.mobilesecurity.model.ContactRepository
import com.example.mobilesecurity.model.SearchItem
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.screens.home.HomeScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class InviteContactViewModel(private val accountRepository: AccountRepository, private val contactRepository: ContactRepository) : ViewModel() {
    val userID = accountRepository.currentUserId

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    val searchResults: StateFlow<List<Contact>> =
        snapshotFlow { searchQuery }
            .combine(contacts) { searchQuery, items ->
                when {
                    searchQuery.isNotEmpty() -> items.filter { item ->
                        item.name.contains(searchQuery, ignoreCase = true)
                    }
                    else -> items
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    @SuppressLint("Range", "Recycle")
    fun getContacts(context: Context) {
        Log.i(contactRepository.TAG, "getting all contacts")
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null
            )
            val listOfContacts = mutableListOf<Contact>()
            while (cursor?.moveToNext() == true) {
                val lookupKey =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                val uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                    lookupKey
                )
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val hasPhone =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                if (phoneCursor?.moveToFirst() == true) {
                    val phoneNumber =
                        phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = Contact(UUID.randomUUID().toString(), name, phoneNumber)
                    Log.d(contactRepository.TAG, "Contact: $contact")
                    contactRepository.addContactSequentially(contact)
                    listOfContacts.add(contact)
                }
            }
            _contacts.value = listOfContacts
            cursor?.close()
        }
    }
}

class InviteContactViewModelFactory(private val accountRepository: AccountRepository, private val contactRepository: ContactRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InviteContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InviteContactViewModel(accountRepository, contactRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}