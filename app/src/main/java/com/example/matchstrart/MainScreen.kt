package com.example.matchstrart.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.matchstrart.viewmodel.FieldViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Default.Home)
    object Fields : Screen("fields", "Terrains", Icons.Default.Stadium)
    object Tournaments : Screen("tournaments", "Tournois", Icons.Default.EmojiEvents)
    object Settings : Screen("settings", "Paramètres", Icons.Default.Settings)
    object FieldDetail : Screen("field_detail/{fieldId}", "Détails", Icons.Default.Info)
    object AddField : Screen("add_field", "Ajouter", Icons.Default.Add)
    object EditField : Screen("edit_field/{fieldId}", "Modifier", Icons.Default.Edit)
    object Matchmaking : Screen("matchmaking", "Recherche Équipe", Icons.Default.Group)
}

@Composable
fun MainScreen(viewModel: FieldViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                NavigationBar {
                    val items = listOf(
                        Screen.Home,
                        Screen.Fields,
                        Screen.Tournaments,
                        Screen.Settings
                    )

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavigationHost(
            navController = navController,
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: FieldViewModel,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel)
        }

        composable(Screen.Fields.route) {
            FieldListScreen(navController, viewModel)
        }

        composable(Screen.Tournaments.route) {
            TournamentScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.FieldDetail.route,
            arguments = listOf(navArgument("fieldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getInt("fieldId") ?: 0
            FieldDetailScreen(navController, viewModel, fieldId)
        }

        composable(Screen.AddField.route) {
            AddEditFieldScreen(navController, viewModel, fieldId = null)
        }

        composable(
            route = Screen.EditField.route,
            arguments = listOf(navArgument("fieldId") { type = NavType.IntType })
        ) { backStackEntry ->
            val fieldId = backStackEntry.arguments?.getInt("fieldId") ?: 0
            AddEditFieldScreen(navController, viewModel, fieldId)
        }

        composable(Screen.Matchmaking.route) {
            MatchmakingScreen(navController, viewModel)
        }
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf(
        Screen.Home.route,
        Screen.Fields.route,
        Screen.Tournaments.route,
        Screen.Settings.route
    )
}