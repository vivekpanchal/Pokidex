package com.vivek.pokidex.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vivek.pokidex.domain.usecase.GetPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    getPokemonUseCase: GetPokemonUseCase,
) : ViewModel() {

    val pokemonList = getPokemonUseCase()
        .cachedIn(viewModelScope) // Cache to prevent re-fetching on rotation
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

}
