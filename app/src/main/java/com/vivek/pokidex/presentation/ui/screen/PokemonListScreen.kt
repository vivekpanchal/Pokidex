package com.vivek.pokidex.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit
) {
    val pokemonList = viewModel.pokemonList.collectAsState().value.collectAsLazyPagingItems()
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
                            filterIconState = false
                        })
                        DropdownMenuItem(text = { Text(text = "Sort by HP") }, onClick = {
                            sortOrder.value = SortOrder.Hp
                            filterIconState = false
                        })
                        DropdownMenuItem(text = { Text(text = "Default") }, onClick = {
                            sortOrder.value = SortOrder.None
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
                    onValueChange = { str->
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

                // Check if the list is currently loading
                val isLoading = pokemonList.loadState.refresh is LoadState.Loading

                if (isLoading) {
                    // Show a loader when the data is initially loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pokemonList.itemCount) { idx ->
                            pokemonList[idx]?.let {
                                PokemonCard(it) {
                                    onCardClick.invoke(it.id)
                                }
                            }
                        }

                        // Handle loading more items at the end of the list
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
    )

}

//preview
@Preview
@Composable
fun PokemonListScreenPreview() {
    PokemonListScreen {}
}








