package com.example.mobilesecurity.screens.groupchat

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobilesecurity.model.Message
import com.example.mobilesecurity.ui.theme.PurpleGrey80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupChatScreen(viewModel : GroupchatViewModel = viewModel(), navController: NavController, groupChatID: String, groupChatName: String, modifier: Modifier = Modifier) {

    val isScrolledToLastItem = remember {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()
    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(groupChatID) {
        viewModel.getGroupChatMessages("$groupChatID")
    }

    var context = LocalContext.current

    viewModel.initializeMapVariables(context)

    var isFirstCompose = rememberSaveable { mutableStateOf(true) }

    if (isFirstCompose.value) {
        isFirstCompose.value = false
        viewModel.getCurrentLocation(
            onGetCurrentLocationFailed = {
                Log.d("getCurrentLocation", "Failed to get current location")
            },
            onGetCurrentLocationSuccess = {
                Log.d("getCurrentLocation", "Successfully got current location: $it")
            },
            context = context
        )
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
                .testTag("chatList"),
            state = listState
        ) {
            items(groupChatMessages) {message ->
                Log.d("Chat Message", "$message")
                //Each Chat message
                ChatMessageItem(message, viewModel)

                coroutineScope.launch {
                    // Animate scroll to the last item
                    if (!isScrolledToLastItem.value) {
                        if (groupChatMessages.isNotEmpty()) {
                            listState.animateScrollToItem(groupChatMessages.size - 1)
                            isScrolledToLastItem.value = true
                        }
                    }
                }
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
                        messageInput.value = ""
                        keyboardController?.hide()
                        isScrolledToLastItem.value = false
                    },
                    Modifier
                        .weight(2f)
                        .height(64.dp)
                        .padding(4.dp, 4.dp)
                        .testTag("chatSend")
                ) {
                    Text(text = "SEND", fontSize = TextUnit(11f, TextUnitType.Sp))
                }

                Button(
                    onClick = {
                        viewModel.getCurrentLocation(
                            onGetCurrentLocationFailed = {
                                Log.d("getCurrentLocation", "Failed to get current location")
                                viewModel.addMessage("Failed to get current location", groupChatID)
                            },
                            onGetCurrentLocationSuccess = {
                                Log.d("getCurrentLocation", "Successfully got current location: $it")
                                val link = "https://www.google.com/maps/search/?api=1&query=${it.first},${it.second}"
                                viewModel.addMessage("Here's my current location:\n$link", groupChatID)
                            },
                            context = context
                        )
                        keyboardController?.hide()
                        isScrolledToLastItem.value = false
                    },
                    Modifier
                        .weight(2f)
                        .height(64.dp)
                        .padding(4.dp, 4.dp)
                        .testTag("chatSend")
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "LocationOn")
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
    if (groupChatMessages.isNotEmpty())
    {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .padding(bottom = 100.dp, end = 10.dp)
                .size(48.dp) // This sets the size of the Box, not the button
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(groupChatMessages.size - 1)
                    }
                },
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Scroll to bottom", modifier = Modifier.size(24.dp)) // Adjust the icon size if needed
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: Message,
    viewModel: GroupchatViewModel
) {
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
                //check if message is a location
                if (message.content.toString().contains("https://www.google.com/maps/search/?api=1&query=")) {
                    //get seperate message and link
                    val messageContent = message.content.toString().split("\n")
                    //create annotated string using messageContent[0] and messageContent[1]
                    Log.d("ClickableText", "messageContent[0]: ${messageContent[0]}")
                    Log.d("ClickableText", "messageContent[1]: ${messageContent[1]}")
                    //extract coordinates from link
                    val coordinates = messageContent[1].split("?api=1&query=")[1].split(",")
                    val lat = coordinates[0].toDouble()
                    val lon = coordinates[1].toDouble()
                    val address = viewModel.geoCoder.getFromLocation(lat, lon, 1)
                    val text = address?.get(0)?.getAddressLine(0).toString()
                    val addressQuery = "https://www.google.com/maps/search/?api=1&&map_action=pano&query=${text.replace(" ", "+")}"
                    Log.d("addressQuery", addressQuery)
                    val annotatedString = buildAnnotatedString {
                        append(messageContent[0])

                        pushStringAnnotation(tag = addressQuery, annotation = addressQuery)
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append(" $text")
                        }
                        pop()
                    }
                    val uriHandler = LocalUriHandler.current

                    //display mini map of the addressQuery
                    Column {
                        AndroidView(factory =
                        { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                settings.javaScriptEnabled = true
                                settings.setSupportZoom(true)
                                settings.builtInZoomControls = true
                                webViewClient = WebViewClient()
                                settings.defaultZoom = WebSettings.ZoomDensity.FAR
                                loadUrl(addressQuery)
                            }
                        },
                            update = { it.loadUrl(addressQuery) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp))
                        ClickableText(
                            text = annotatedString,
                            onClick = {
                                //handle click on link
                                annotatedString.getStringAnnotations(tag = addressQuery, start = 0, end = annotatedString.length).firstOrNull()?.let { annotation ->
                                    Log.d("ClickableText", "Link clicked: ${annotation.item}")
                                    uriHandler.openUri(annotation.item)
                                }
                            }
                        )
                    }
                }
                else {
                    Text(
                        text = message.content.toString()
                    )
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Date?): String {
    timestamp ?: return ""
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(timestamp)
}

