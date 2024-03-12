package com.example.mobilesecurity.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.R
import com.example.mobilesecurity.Screen
import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import com.example.mobilesecurity.ui.theme.MobileSecurityTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.firestore.FirebaseFirestore

data class teamMemberData(
    val userId: String,
    val admin: Boolean,
    var userName: String = "", // Add userName field
    var userRole: String = ""  // Add userRole field
)

data class userData(
    val userName: String,
    val userRole: String
)


//TODO
//1. Change teamdocumentref to use mutablestate once chat set up
//2. Change userisadmin to get whether userr is admin or not

@Composable
fun makeadmin(viewModel: makeadminViewModel = viewModel(), navController: NavController) {
    val userIsAdmin = true
    var chatGroupName by remember { mutableStateOf("") } // chat group name
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var teamMembers by remember { mutableStateOf<List<teamMemberData>>(emptyList()) }
    var userDataMap by remember { mutableStateOf<Map<String, userData>>(emptyMap()) }

    val db = FirebaseFirestore.getInstance()

    // Fetch team data from Firestore
    val teamDocumentRef = db.collection("teams")
        .document("8b14b87a-9277-45fd-a7e9-8a9797da2a00") // REMEMBER TO CHANGE TO USE MUTABLESTATE ONCE CHAT IS DONE
    teamDocumentRef.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot != null && documentSnapshot.exists()) {
            // Document exists, extract team name and members
            chatGroupName = documentSnapshot.getString("teamName") ?: ""
            val members =
                documentSnapshot.get("teamMembers") as? List<Map<String, Any>> ?: emptyList()
            teamMembers = members.map {
                teamMemberData(
                    userId = it["userId"] as? String ?: "",
                    admin = it["admin"] as? Boolean ?: false
                )
            }

            // Fetch user data for all users
            val userIds = teamMembers.map { it.userId }
            val userDocumentRefs = userIds.map { db.collection("users").document(it) }
            userDocumentRefs.forEachIndexed { index, userDocumentRef ->
                userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userName = documentSnapshot.getString("userName") ?: ""
                        val userRole = documentSnapshot.getString("userRole") ?: ""
                        // Update the userDataMap with fetched user data
                        userDataMap = userDataMap + (userIds[index] to userData(userName, userRole))
                    } else {
                        // Handle document not found or other errors
                    }
                }.addOnFailureListener { exception ->
                    // Handle error
                }
            }
        } else {
            // Document does not exist or has been deleted
            // Handle error gracefully
            // For example, you can show a message indicating that the team data is not available
            teamMembers = emptyList() // Set teamMembers to empty list
            chatGroupName =
                "Team Data Not Available" // Set chatGroupName to indicate data is not available
        }
    }.addOnFailureListener { exception ->
        // Error fetching team data
        // Handle error gracefully
        // For example, you can show a message indicating that the team data could not be fetched
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Centered large text at the top of the page
        Text(
            text = chatGroupName,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Search")
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(teamMembers.filter {
                it.userId.contains(searchText.text, ignoreCase = true)
            }) { member ->
                val userData = userDataMap[member.userId]
                if (userData?.userName?.isNotBlank() == true) { // Check if user name is not empty
                    Row(
                        modifier = Modifier.padding(vertical = 7.dp)
                    ) {
                        // Profile picture circle
                        Image(
                            painter = painterResource(id = R.drawable.tum_0), // Change to your image resource
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        // Spacer between profile picture and text
                        Spacer(modifier = Modifier.width(16.dp))
                        // Display name and profile description
                        Column {
                            userData.let {
                                Text(
                                    text = it?.userName ?: "",
                                    fontSize = 25.sp, //25 default
                                    overflow = TextOverflow.Ellipsis
                                )
                                // Add user role if needed
                                Text(
                                    text = it?.userRole ?: "",
                                    fontSize = 18.sp
                                )
                            }
                        }
                        // Spacer between text and button
                        Spacer(modifier = Modifier.weight(1f))
                        // Button
                        if (userIsAdmin && !member.admin) {
                            Button(
                                onClick = {
                                    // Update Firestore document to set admin to true for the selected user
                                    val docRef = db.collection("teams").document("8b14b87a-9277-45fd-a7e9-8a9797da2a00")
                                    docRef.get().addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            val teamMembersList = documentSnapshot.get("teamMembers") as? List<Map<String, Any>> ?: emptyList()
                                            val updatedTeamMembersList = teamMembersList.map { teamMember ->
                                                if (teamMember["userId"] == member.userId) {
                                                    mapOf(
                                                        "userId" to teamMember["userId"],
                                                        "admin" to true
                                                    )
                                                } else {
                                                    teamMember
                                                }
                                            }
                                            docRef.update("teamMembers", updatedTeamMembersList)
                                                .addOnSuccessListener {
                                                    // Update local state to reflect the change
                                                    teamMembers = teamMembers.map { teamMember ->
                                                        if (teamMember.userId == member.userId) {
                                                            teamMember.copy(admin = true)
                                                        } else {
                                                            teamMember
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener { exception ->
                                                    // Handle error
                                                }
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Make")
                                    Text(text = "Admin")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}