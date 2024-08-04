package com.vivek.pokidex.domain.usecase

import androidx.paging.PagingData
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting a flow of PagingData of Pokemon.
 */
class GetPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    fun execute(): Flow<PagingData<Pokemon>> = repository.getPokemon()
}
