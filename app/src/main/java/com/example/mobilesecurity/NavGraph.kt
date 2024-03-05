package com.example.mobilesecurity

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobilesecurity.screens.sign_in.SignInScreen
import com.example.mobilesecurity.screens.sign_in.SignInViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilesecurity.model.AccountRepository
import com.example.mobilesecurity.model.SearchRepository
import com.example.mobilesecurity.screens.createTeam.CreateTeamViewModel
import com.example.mobilesecurity.screens.home.HomeScreen
import com.example.mobilesecurity.screens.home.HomeScreenViewModel
import com.example.mobilesecurity.screens.home.HomeScreenViewModelFactory
import com.example.mobilesecurity.screens.profile.ProfileScreen
import com.example.mobilesecurity.screens.profile.ProfileViewModel
import com.example.mobilesecurity.screens.profile.ProfileViewModelFactory
import com.example.mobilesecurity.screens.search.SearchScreen
import com.example.mobilesecurity.screens.search.SearchScreenViewModel
import com.example.mobilesecurity.screens.search.SearchScreenViewModelFactory
import com.example.mobilesecurity.screens.sign_in.SignInViewModelFactory
import com.example.mobilesecurity.screens.sign_up.SignUpScreen
import com.example.mobilesecurity.screens.sign_up.SignUpViewModel
import com.example.mobilesecurity.screens.sign_up.SignUpViewModelFactory
import com.example.mobilesecurity.screens.search.SearchScreen
import com.example.mobilesecurity.screens.createTeam.CreateTeamScreen
import com.example.mobilesecurity.screens.createTeam.CreateTeamViewModelFactory
import com.example.mobilesecurity.screens.profile.ViewProfileViewModelFactory
import com.example.mobilesecurity.screens.profile.ViewProfileScreen
import com.example.mobilesecurity.screens.profile.ViewProfileViewModel


sealed class Screen(val route: String) {
    object SignInScreen : Screen(route = "signin_screen")
    object SignUpScreen : Screen(route = "signup_screen")
    object HomeScreen: Screen(route = "home_screen")
    object makeadminScreen: Screen(route= "make_admin")
    object CreateTeamScreen: Screen(route = "createTeam_screen")
    object SearchScreen: Screen(route = "search_screen")
    object ProfileScreen: Screen(route = "profile_screen")
    object ViewProfileScreen: Screen(route = "profile_screen/{userId}")
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
    AccountRepository(), SearchRepository()
)

val profileViewModelFactory = ProfileViewModelFactory(
    AccountRepository()
)

val createTeamViewModelFactory = CreateTeamViewModelFactory(
    AccountRepository() , SearchRepository()
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

        composable(Screen.SearchScreen.route) {
            val viewModel: SearchScreenViewModel = viewModel(factory = searchScreenViewModelFactory)
            SearchScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.ProfileScreen.route) {
            val viewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)
            ProfileScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.CreateTeamScreen.route) {
            val viewModel: CreateTeamViewModel = viewModel(factory = createTeamViewModelFactory)
            CreateTeamScreen(viewModel, navController = navController)
        }

        composable(Screen.ViewProfileScreen.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            Log.d("ViewProfileScreen", "userId: $userId")
            val viewProfileViewModelFactory = ViewProfileViewModelFactory(
                AccountRepository(), userId
            )
            val viewModel: ViewProfileViewModel = viewModel(factory = viewProfileViewModelFactory)
            ViewProfileScreen(viewModel = viewModel, navController = navController)
        }
    }
}