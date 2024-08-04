package com.vivek.pokidex.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.vivek.pokidex.data.local.entity.PokemonEntity
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.presentation.ui.components.PokemonCard
import com.vivek.pokidex.presentation.ui.components.SearchBarPokeDex
import com.vivek.pokidex.presentation.ui.components.SortMenu
import com.vivek.pokidex.presentation.ui.components.SortOrder
import com.vivek.pokidex.presentation.viewModel.PokemonViewModel
import com.vivek.pokidex.utils.Resource
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit
) {
//    val pokemonList = viewModel.pokemonList.collectAsState().value.collectAsLazyPagingItems()
    val searchQuery = remember { mutableStateOf("") }
    val sortOrder = remember { mutableStateOf(SortOrder.None) }

    var filterIconState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PokeDex",
                        modifier = Modifier.padding(start = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = { filterIconState = !filterIconState }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Sort"
                        )
                    }
                    DropdownMenu(
                        expanded = filterIconState,
                        onDismissRequest = { filterIconState = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "Sort by Level") }, onClick = {
                            sortOrder.value = SortOrder.Level
                            viewModel.sortPokemon(sortOrder.value)
                            filterIconState = false
                        })
                        DropdownMenuItem(text = { Text(text = "Sort by HP") }, onClick = {
                            sortOrder.value = SortOrder.Hp
                            viewModel.sortPokemon(sortOrder.value)
                            filterIconState = false
                        })
                        DropdownMenuItem(text = { Text(text = "Default") }, onClick = {
                            sortOrder.value = SortOrder.None
                            viewModel.sortPokemon(sortOrder.value)
                            filterIconState = false
                        })
                    }
                },
            )
        },
        content = { paddingValues ->
            // Your screen content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                TextField(
                    value = searchQuery.value,
                    onValueChange = { str ->
                        searchQuery.value = str
                        viewModel.searchQuery(searchQuery.value)
                    },
                    placeholder = { Text("Search by name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                )

                val pokemonState by viewModel.pokemon.collectAsState()

                when (pokemonState) {
                    is Resource.Error -> {
                        // Show an error message
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Failed to load data: ${pokemonState.message}")
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.fetchPokemon() }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }

                    is Resource.Loading -> {
                        // Show initial loading indicator
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        // Show the list of Pokémon
                        val pokemonList = pokemonState.data ?: emptyList()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            itemsIndexed(pokemonList) { index, pokemon ->
                                PokemonCard(pokemon) {
                                    onCardClick.invoke(pokemon.id)
                                }

                                // Trigger loading more when near the bottom
                                if (index >= pokemonList.size - 1) {
                                    LaunchedEffect(Unit) {
                                        viewModel.loadMore()
                                    }
                                }

                            }

                            // Bottom loading indicator
                            item {
                                if (pokemonState is Resource.Loading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}








