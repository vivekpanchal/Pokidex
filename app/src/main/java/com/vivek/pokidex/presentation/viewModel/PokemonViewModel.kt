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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
) : ViewModel() {

    private val _pokemon = MutableStateFlow<Resource<List<Pokemon>>>(Resource.Loading())
    val pokemon: StateFlow<Resource<List<Pokemon>>> = _pokemon

    private var currentSortOrder = SortOrder.None
    private var currentFilter: String? = null

    var isLoadingMore = false

    private val _loadMoreTrigger = MutableSharedFlow<Boolean>()
    val loadMoreTrigger = _loadMoreTrigger.asSharedFlow()


    init {
        fetchPokemon()
    }


    fun fetchPokemon() {
        viewModelScope.launch {
            getPokemonUseCase.execute(currentSortOrder, currentFilter)
                .collect { _pokemon.value = it }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            getPokemonUseCase.repository.loadMorePokemon().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val currentData = _pokemon.value.data ?: emptyList()
                        _pokemon.value = Resource.Success(currentData + resource.data.orEmpty())
                        isLoadingMore = false
                        _loadMoreTrigger.emit(false)  // Reset trigger
                    }
                    is Resource.Loading -> {
                        _pokemon.value = Resource.Loading(_pokemon.value.data)
                    }
                    is Resource.Error -> {
                        _pokemon.value = Resource.Error(Throwable(resource.message), _pokemon.value.data)
                        isLoadingMore = false
                        _loadMoreTrigger.emit(false)  // Reset trigger

                    }
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
