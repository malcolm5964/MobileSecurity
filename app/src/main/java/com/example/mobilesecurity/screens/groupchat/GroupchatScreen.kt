package com.example.mobilesecurity.screens.groupchat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.ImeAction
import com.example.mobilesecurity.model.Message
import com.example.mobilesecurity.ui.theme.PurpleGrey80

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
                onClick = { /*TODO: route to previous page*/ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back Button")
            }

            Text(
                text = "$groupChatName", //todo: retrieve teamname from db
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            ElevatedButton(
                onClick = { /*TODO: route to view members page*/ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "View all members")
            }
        }

        Divider(thickness = 2.dp)

        //todo: add scrollable list to display chat messages from realtime db
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("chatList")
        ) {
            items(groupChatMessages) {message ->
                Log.d("Chat Message", "$message")
                ChatMessageItem(message)
            }
        }

        Divider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ){
            TextField(
                value = messageInput.value,
                onValueChange = {messageInput.value = it},
                modifier = Modifier
                    .testTag("inputRepo"),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() })
            )


            Button(
                onClick = {
                    viewModel.addMessage(messageInput.value, groupChatID)
                    keyboardController?.hide()},
                Modifier
                    .testTag("chatSend")
            ) {
                Text(text = "SEND")
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: Message) {
    Column {
        Box(
            modifier = Modifier
                .align(if (message.userID == "3RkfEdhAh8R7Nle3b4iWsiFMn4O2") Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.userID == "3RkfEdhAh8R7Nle3b4iWsiFMn4O2") 48f else 0f,
                        bottomEnd = if (message.userID == "3RkfEdhAh8R7Nle3b4iWsiFMn4O2") 0f else 48f
                    )
                )
                .background(PurpleGrey80)
                .padding(16.dp)
        ) {
            Text(text = message.content.toString())
        }
    }

}

