package com.example.mobilesecurity.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilesecurity.R
import com.example.mobilesecurity.screens.home.BottomNavigationBar
import com.example.mobilesecurity.ui.theme.Purple40

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.photo_2024_03_08_15_21_18),
                    contentDescription = "Auth image",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp)
                        .width(300.dp)
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Username Field
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Username", modifier = Modifier.padding(start = 16.dp, end = 16.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(width = 2.dp, color = Purple40),
                                shape = RoundedCornerShape(50)
                            ),
                        singleLine = true,
                        value = viewModel.username.value,
                        onValueChange = { viewModel.username.value = it },
                        label = { Text("New Username") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Password Field
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Password", modifier = Modifier.padding(start = 16.dp, end = 16.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(width = 2.dp, color = Purple40), shape = RoundedCornerShape(50)),
                        singleLine = true,
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }

            // Buttons at the bottom
            Column {
                Button(
                    onClick = {
                        viewModel.updateUserInfo(viewModel.username.value, viewModel.password.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Update")
                }
            }
        }
    }
}

