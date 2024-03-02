package com.example.mobilesecurity.screens.profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.tum_0),
                contentDescription = "Auth image",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Username")

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 4.dp)
                        .border(
                            BorderStroke(width = 2.dp, color = Purple40),
                            shape = RoundedCornerShape(50)
                        ),
                    singleLine = true,
                    value = viewModel.username.value,
                    onValueChange = {viewModel.updateUsername(it)})



            }
        }
    }

}