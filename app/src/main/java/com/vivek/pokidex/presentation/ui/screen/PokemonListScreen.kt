package com.vivek.pokidex.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.vivek.pokidex.R
import com.vivek.pokidex.presentation.ui.components.PokemonCard
import com.vivek.pokidex.presentation.viewModel.PokemonViewModel


@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit
) {
    val pokemonList = viewModel.pokemonList.collectAsLazyPagingItems()
    Surface() {

        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.size(20.dp))

            Image(
                painter = painterResource(id = R.drawable.pokemon_logo), // Replace with your icon
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Handling states
                when {
                    pokemonList.loadState.refresh is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    pokemonList.itemCount == 0 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Pokémon found",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),  // 2-column grid
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(pokemonList.itemCount) { idx ->
                                pokemonList[idx]?.let { pokemon ->
                                    PokemonCard(pokemon) {
                                        //  onCardClick(pokemon.name)
                                    }
                                }
                            }

                            when (pokemonList.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }

                                is LoadState.Error -> {
                                    item {
                                        Text(
                                            text = "Error loading more Pokémon. Try again.",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}


//preview
@Preview
@Composable
fun PokemonListScreenPreview() {
    PokemonListScreen {}
}








