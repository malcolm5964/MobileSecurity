    package com.example.mobilesecurity.screens.sign_in

    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Email
    import androidx.compose.material.icons.filled.Lock
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavController
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.example.mobilesecurity.R
    import com.example.mobilesecurity.ui.theme.Purple40
    import androidx.compose.ui.platform.LocalContext
    import com.google.firebase.firestore.FirebaseFirestore
    import javax.crypto.Cipher
    import javax.crypto.spec.SecretKeySpec
    import java.util.Base64
    import com.google.firebase.firestore.Query



    @Composable
    fun SignInScreen(viewModel : SignInViewModel = viewModel(), navController: NavController, modifier: Modifier = Modifier) {
        val context = LocalContext.current

        val email = viewModel.email.collectAsState()
        val password = viewModel.password.collectAsState()

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.photo_2024_03_08_15_21_18),
                contentDescription = "Auth image",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .width(300.dp)
                    .height(300.dp)
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp))

            OutlinedTextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = Purple40),
                        shape = RoundedCornerShape(50)
                    ),
                value = email.value,
                onValueChange = { viewModel.updateEmail(it)},
                placeholder = { Text("Email") },
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
            )

            OutlinedTextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = Purple40),
                        shape = RoundedCornerShape(50)
                    ),
                value = password.value,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = { Text("Password") },
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Email") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp))

            Button(
                onClick = {
                    viewModel.onSignInClick(navController, context)
                          },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    modifier = modifier.padding(0.dp, 6.dp)
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp))

            TextButton(onClick = { navController.navigate("signup_screen") }) {
                Text(text = "Don't have an account? Click here to Sign Up!", fontSize = 16.sp)
            }
        }
    }




