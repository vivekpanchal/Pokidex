package com.vivek.pokidex.domain.usecase

import androidx.paging.PagingData
import com.vivek.pokidex.data.remote.models.PokemonItem
import com.vivek.pokidex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting a flow of PagingData of Pokemon.
 */
class GetPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(pageSize: Int = 50): Flow<PagingData<PokemonItem>> {
        return repository.fetchPokemonList(pageSize)
    }
}
