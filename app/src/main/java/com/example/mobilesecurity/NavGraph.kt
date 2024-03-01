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
import com.example.mobilesecurity.screens.home.makeadmin
import com.example.mobilesecurity.screens.home.makeadminViewModel
import com.example.mobilesecurity.screens.home.makeadminViewModelFactory
import com.example.mobilesecurity.screens.sign_in.SignInViewModelFactory
import com.example.mobilesecurity.screens.sign_up.SignUpScreen
import com.example.mobilesecurity.screens.sign_up.SignUpViewModel
import com.example.mobilesecurity.screens.sign_up.SignUpViewModelFactory
import com.example.mobilesecurity.screens.search.SearchScreen
import com.example.mobilesecurity.screens.search.SearchScreenViewModel
import com.example.mobilesecurity.screens.search.SearchScreenViewModelFactory



sealed class Screen(val route: String) {
    object SignInScreen : Screen(route = "signin_screen")
    object SignUpScreen : Screen(route = "signup_screen")
    object HomeScreen: Screen(route = "home_screen")

    object makeadminScreen: Screen(route= "make_admin")

    object SearchScreen: Screen(route = "search_screen")
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

val searchScreenViewModelFactory = SearchScreenViewModelFactory(
    AccountRepository()
)

val makeadminViewModelFactory = makeadminViewModelFactory(
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

        composable(Screen.makeadminScreen.route) {
            val viewModel: makeadminViewModel = viewModel(factory = makeadminViewModelFactory)
            makeadmin(viewModel, navController)
        }

        composable(Screen.SearchScreen.route) {
            val viewModel: SearchScreenViewModel = viewModel(factory = searchScreenViewModelFactory)
            SearchScreen(viewModel = viewModel, navController = navController)
        }

    }
}