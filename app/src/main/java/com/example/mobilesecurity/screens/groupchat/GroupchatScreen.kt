package com.example.mobilesecurity.screens.groupchat

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.mobilesecurity.model.Message
import com.example.mobilesecurity.ui.theme.PurpleGrey80
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupChatScreen(viewModel : GroupchatViewModel = viewModel(), navController: NavController, groupChatID: String, groupChatName: String, modifier: Modifier = Modifier) {

    LaunchedEffect(groupChatID) {
        viewModel.getGroupChatMessages("$groupChatID")
    }

    var messageInput = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val groupChatMessages by viewModel.groupChatMessages.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick =
                {
                    Log.d("destination", navController.previousBackStackEntry?.destination?.route.toString())
                    if (navController.previousBackStackEntry?.destination?.route == "search_screen") {
                        navController.navigate("search_screen")
                    }
                    else {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back Button")
            }

            Text(
                text = "$groupChatName", //todo: retrieve teamname from db
                modifier = Modifier
                    .weight(2f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            ElevatedButton(
                onClick = {navController.navigate("makeadmin_screen/${groupChatID}")},
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "View all members")
            }
        }

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(0.dp, 0.dp, 0.dp, 8.dp)
        )

        //todo: add scrollable list to display chat messages from realtime db
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("chatList")
        ) {
            items(groupChatMessages) {message ->
                Log.d("Chat Message", "$message")
                //Each Chat message
                ChatMessageItem(message, viewModel)
            }
        }

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(0.dp, 8.dp, 0.dp, 0.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ){
            //if user is not in team, hide input field
            Log.d("group chat team current user id", viewModel.currentUserId)
            viewModel.teamId.value = groupChatID
            Log.d("group chat team id", viewModel.teamId.value)
            viewModel.getTeam(viewModel.teamId.value)
            val team = viewModel.team.collectAsState()
            Log.d("group chat team", team.toString())
            Log.d("isCurrentUserInsideTeam", viewModel.isCurrentUserInsideTeam.value.toString())
            if (viewModel.isCurrentUserInsideTeam.value) {
                OutlinedTextField(
                    singleLine = true,
                    modifier = modifier
                        .weight(5f)
                        .padding(4.dp, 4.dp)
                        .border(
                            BorderStroke(width = 2.dp, color = Color.Black),
                            shape = RoundedCornerShape(50)
                        ),
                    value = messageInput.value,
                    onValueChange = { messageInput.value = it },
                    placeholder = { Text("Message Input") }
                )

                Button(
                    onClick = {
                        viewModel.addMessage(messageInput.value, groupChatID)
                        keyboardController?.hide()},
                    Modifier
                        .weight(2f)
                        .height(64.dp)
                        .padding(4.dp, 4.dp)
                        .testTag("chatSend")
                ) {
                    Text(text = "SEND")
                }
            }
            else
            {
                Button(onClick = { viewModel.addTeamMember() }) {
                    Text(text = "Join Team to send messages")
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: Message, viewModel: GroupchatViewModel) {
    Column(
        modifier = Modifier.padding(0.dp, 2.dp)
    ) {
        Box(
            modifier = Modifier
                //.align(if (message.userID == viewModel.userID) Alignment.End else Alignment.Start)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.userID == viewModel.userID) 48f else 0f,
                        bottomEnd = if (message.userID == viewModel.userID) 0f else 48f
                    )
                )
                .background(PurpleGrey80)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = message.username.toString(),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(
                        modifier = Modifier.padding(1.dp)
                    )
                    Text(
                        textAlign = TextAlign.End,
                        text = formatTimestamp(message.timestamp),
                        color = Color.Gray
                    )
                }
                Spacer(
                    modifier = Modifier.padding(1.dp)
                )
                Text(
                    text = message.content.toString()
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Date?): String {
    timestamp ?: return ""
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(timestamp)
}

