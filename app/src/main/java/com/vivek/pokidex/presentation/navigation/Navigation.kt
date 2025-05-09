package com.vivek.pokidex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vivek.pokidex.presentation.ui.screen.PokemonListScreen
import com.vivek.pokidex.presentation.ui.screen.Screen


@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen { pokemonId ->
                navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
            }
        }
//        composable(
//            route = Screen.PokemonDetail.route
//        ) { backStackEntry ->
//            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: return@composable
//            PokemonDetailScreen(pokemonId)
//        }
    }
}
