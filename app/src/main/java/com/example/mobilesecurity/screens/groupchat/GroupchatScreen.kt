package com.example.mobilesecurity.screens.groupchat

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupChatScreen(viewModel : GroupchatViewModel = viewModel(), navController: NavController, groupChatID: String, groupChatName: String, modifier: Modifier = Modifier) {

    var messageInput = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

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

        }

        Divider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )

/*        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                *//*OutlinedTextField( //todo: retrieve user id from session
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp, 4.dp)
                        .border(
                            BorderStroke(width = 2.dp, color = Color.Black),
                            shape = RoundedCornerShape(50)
                        ),
                    value = null, //todo
                    //onValueChange = { viewModel.writeGroupchatMessage(it) }, //todo
                    placeholder = { Text("Message Team Name") }, //todo: get team name from db
                )*//*

                Spacer(modifier = Modifier.padding(4.dp))

                ElevatedButton(
                    onClick = { *//*TODO*//* },
                    modifier = Modifier.width(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Rounded.Send, contentDescription = "Send Message")
                }
            }
        }*/


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

/*
@Composable
fun groupchatMessageItem(groupchatMessage: GroupchatMessage){ //todo
    Text(text = "${groupchatMessage.userId}: ${groupchatMessage.content}")
}*/
