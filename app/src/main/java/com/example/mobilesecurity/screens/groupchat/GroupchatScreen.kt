package com.example.mobilesecurity.screens.groupchat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.ImageButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import com.example.mobilesecurity.ui.theme.MobileSecurityTheme

@Composable
fun GroupChatScreen(viewModel : GroupchatViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    //val groupchatMessages = remember { mutableStateListOf<GroupchatMessage>() }
    // todo: retrieve and store chatmessage in realtime db

    //todo: retrieve user id from session, retrieve groupchat messages, retrieve teamname data

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
                text = "Team Name", //todo: retrieve teamname from db
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
        /*LazyColumn(){
            items(groupchatMessages) { groupchatMessage ->
                groupchatMessageItem(groupchatMessage)
            }
        }*/

        Divider(thickness = 2.dp)

        Column(
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
                /*OutlinedTextField( //todo: retrieve user id from session
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
                )*/

                Spacer(modifier = Modifier.padding(4.dp))

                ElevatedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Rounded.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}

/*
@Composable
fun groupchatMessageItem(groupchatMessage: GroupchatMessage){ //todo
    Text(text = "${groupchatMessage.userId}: ${groupchatMessage.content}")
}*/
