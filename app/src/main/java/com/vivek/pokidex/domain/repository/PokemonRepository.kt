package com.vivek.pokidex.domain.repository

import androidx.paging.PagingData
import com.vivek.pokidex.data.remote.models.PokemonItem
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    fun fetchPokemonList(pageSize: Int): Flow<PagingData<PokemonItem>>
//    suspend fun getPokemonDetail(pokemonId: String): Pokemon?
}
