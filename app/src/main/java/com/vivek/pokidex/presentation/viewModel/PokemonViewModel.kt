package com.vivek.pokidex.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.vivek.pokidex.data.local.entity.PokemonEntity
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.domain.usecase.GetPokemonDetailUseCase
import com.vivek.pokidex.domain.usecase.GetPokemonUseCase
import com.vivek.pokidex.presentation.ui.components.SortOrder

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {


    // State for the list of Pokemon using Paging
    private val _pokemonList = MutableStateFlow(getPokemonUseCase.execute().cachedIn(viewModelScope))
    val pokemonList: StateFlow<Flow<PagingData<Pokemon>>> = _pokemonList.asStateFlow()


    fun searchQuery(query: String) {
        _pokemonList.value = if (query.isEmpty()) {
            getPokemonUseCase.execute().cachedIn(viewModelScope)
        } else {
            getPokemonUseCase.execute().cachedIn(viewModelScope).map { pagingData ->
                pagingData.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true)
                }
            }
        }
    }

//    // Function to sort and update the paging data
//    fun sortPokemon(sortOrder: SortOrder) {
//        _pokemonList.value = getPokemonUseCase.execute().cachedIn(viewModelScope).map { pagingData ->
//            when (sortOrder) {
//                SortOrder.Level -> pagingData.map { it }.sortedByDescending { it.level.toIntOrNull() }
//                SortOrder.Hp -> pagingData.map { it }.sortedByDescending { it.hp.toIntOrNull() }
//                SortOrder.None -> pagingData
//            }
//        }
//    }


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
