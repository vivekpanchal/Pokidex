package com.vivek.pokidex.presentation.ui.screen


sealed class Screen(val route: String) {
    data object PokemonList : Screen("pokemon_list")
    data object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: String) = "pokemon_detail/$pokemonId"
    }
}
