package com.example.mobilesecurity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobilesecurity.screens.sign_in.SignInScreen
import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.screens.home.HomeScreen
import com.example.mobilesecurity.screens.home.HomeScreenViewModel
import com.example.mobilesecurity.screens.home.HomeScreenViewModelFactory
import com.example.mobilesecurity.screens.sign_in.SignInViewModelFactory
import com.example.mobilesecurity.screens.sign_up.SignUpScreen
import com.example.mobilesecurity.screens.sign_up.SignUpViewModel
import com.example.mobilesecurity.screens.sign_up.SignUpViewModelFactory

sealed class Screen(val route: String) {
    object SignInScreen : Screen(route = "signin_screen")
    object SignUpScreen : Screen(route = "signup_screen")
    object HomeScreen: Screen(route = "home_screen")
}

//ViewModel Factory
val signInViewModelFactory = SignInViewModelFactory(
    AccountRepository()
)

val signUpViewModelFactory = SignUpViewModelFactory(
    AccountRepository()
)

val homeScreenViewModelFactory = HomeScreenViewModelFactory(
    AccountRepository()
)



@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "signin_screen",
        ){

        composable(Screen.SignInScreen.route) {
            val viewModel: SignInViewModel = viewModel(factory = signInViewModelFactory)
            SignInScreen(viewModel, navController)
        }

        composable(Screen.SignUpScreen.route) {
            val viewModel: SignUpViewModel = viewModel(factory = signUpViewModelFactory)
            SignUpScreen(viewModel, navController)
        }

        composable(Screen.HomeScreen.route) {
            val viewModel: HomeScreenViewModel = viewModel(factory = homeScreenViewModelFactory)
            HomeScreen(viewModel, navController)
        }

    }
}