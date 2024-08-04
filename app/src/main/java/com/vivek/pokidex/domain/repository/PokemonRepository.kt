package com.vivek.pokidex.domain.repository

import androidx.paging.PagingData
import com.vivek.pokidex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    fun getPokemon(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetail(pokemonId: String): Pokemon?
}
