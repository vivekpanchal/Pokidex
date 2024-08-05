package com.vivek.pokidex.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.presentation.ui.components.ErrorScreen
import com.vivek.pokidex.presentation.ui.components.LoadingScreen
import com.vivek.pokidex.presentation.ui.components.PokemonCard
import com.vivek.pokidex.presentation.ui.components.SortOrder
import com.vivek.pokidex.presentation.viewModel.PokemonViewModel
import com.vivek.pokidex.utils.Resource
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit
) {

    val scrollState = rememberLazyListState()
    val isItemReachedEndScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == scrollState.layoutInfo.totalItemsCount - 1
        }
    }

    val pokemonState by viewModel.pokemon.collectAsState()
    val isLoadMore by viewModel.isLoadMoreInProgress.collectAsState(initial = false)
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
                    value =searchQuery.value,
                    onValueChange ={ str->
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



                LaunchedEffect(key1 = isItemReachedEndScroll) {
                    //reached end of the scroll loading more data
                    Timber.d("Reached end of the scroll +++++")
                    Timber.d("current Items list -> ${pokemonState.data?.size}")
                    viewModel.loadMore()
                }




                when (pokemonState) {
                    is Resource.Error -> {
                        // Show an error message
                        ErrorScreen(
                            message = (pokemonState as Resource.Error).message
                                ?: "Something went wrong"
                        )
                    }

                    is Resource.Loading -> {
                        if ((pokemonState as Resource.Loading).data.isNullOrEmpty()) {
                            LoadingScreen()
                        } else {
                            LazyColumnContent(
                                items = (pokemonState as Resource.Loading).data.orEmpty(),
                                listState = scrollState,
                                onCardClick = onCardClick,
                                onLoadMore = { viewModel.loadMore() },
                                isLoadingMore = isLoadMore
                            )
                        }
                    }

                    is Resource.Success -> {
                        // Show the list of Pokémon
                        LazyColumnContent(
                            items = (pokemonState as Resource.Success).data ?: emptyList(),
                            listState = scrollState,
                            onCardClick = onCardClick,
                            onLoadMore = { viewModel.loadMore() },
                            isLoadingMore = isLoadMore
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun LazyColumnContent(
    items: List<Pokemon>,
    listState: LazyListState,
    onCardClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            PokemonCard(item) {
                onCardClick(item.id)
            }
        }

        // Show loading indicator at the bottom when loading more
        if (isLoadingMore) {
            item {
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


/**
 * List Top APP bar
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ListTopBar(
    filterIconState: Boolean,
    sortOrder: MutableState<SortOrder>,
    viewModel: PokemonViewModel
) {
    var filterIconState1 = filterIconState

}











