package com.vivek.pokidex.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.vivek.pokidex.data.local.entity.PokemonEntity
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.domain.repository.PokemonRepository
import com.vivek.pokidex.domain.usecase.GetPokemonDetailUseCase
import com.vivek.pokidex.domain.usecase.GetPokemonUseCase
import com.vivek.pokidex.presentation.ui.components.SortOrder
import com.vivek.pokidex.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
) : ViewModel() {

    private val _pokemon = MutableStateFlow<Resource<List<Pokemon>>>(Resource.Loading())
    val pokemon: StateFlow<Resource<List<Pokemon>>> = _pokemon
    private val addedPokemonIds = mutableSetOf<String>()

    private var currentSortOrder = SortOrder.None
    private var currentFilter: String? = null

    private val _isLoadMoreInProgress = MutableStateFlow(false)
    val isLoadMoreInProgress: StateFlow<Boolean> = _isLoadMoreInProgress

    private var isRequestInProgress = false

    private val cachedPokemonList = mutableListOf<Pokemon>()


    init {
        fetchPokemon()
    }


    fun fetchPokemon() {
        viewModelScope.launch {
            cachedPokemonList.clear()
            getPokemonUseCase.execute(currentSortOrder, currentFilter)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            cachedPokemonList.addAll(it.data ?: emptyList())
                            _pokemon.value = Resource.Success(cachedPokemonList.toList())
                        }

                        is Resource.Loading -> {
                            _pokemon.value = Resource.Loading(cachedPokemonList.toList())
                        }

                        is Resource.Error -> {
                            _pokemon.value =
                                Resource.Error(Throwable(it.message), cachedPokemonList.toList())
                        }
                    }
                }
        }
    }


    fun loadMore() {
        if (!isRequestInProgress && !isLoadMoreInProgress.value) {
            isRequestInProgress = true
            _isLoadMoreInProgress.value = true
            viewModelScope.launch {
                delay(2000)
                getPokemonUseCase.repository.loadMorePokemon()
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                cachedPokemonList.addAll(resource.data ?: emptyList())
                                _pokemon.value = Resource.Success(cachedPokemonList.toList())
                                _isLoadMoreInProgress.value = false
                            }

                            is Resource.Error -> {
                                _pokemon.value = Resource.Error(
                                    Throwable(resource.message),
                                    cachedPokemonList.toList()
                                )
                                _isLoadMoreInProgress.value = false
                            }

                            else -> {
                                _isLoadMoreInProgress.value = false
                            }
                        }
                        isRequestInProgress = false
                    }
            }
        }
    }

    fun searchQuery(query: String) {
        currentFilter = query
        fetchPokemon()
    }

    fun sortPokemon(sortOrder: SortOrder) {
        currentSortOrder = sortOrder
        fetchPokemon()
    }


    // State for Pokemon detail
    private val _pokemonDetail = MutableStateFlow<Pokemon?>(null)
    val pokemonDetail: StateFlow<Pokemon?> = _pokemonDetail.asStateFlow()

    // Function to load Pokemon detail
    fun loadPokemonDetail(pokemonId: String) {
        viewModelScope.launch {
            val detail = getPokemonDetailUseCase(pokemonId)
            _pokemonDetail.value = detail
        }
    }
}
