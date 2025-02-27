package com.vivek.pokidex.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vivek.pokidex.data.remote.ApiService
import com.vivek.pokidex.data.remote.PokemonPagingSource
import com.vivek.pokidex.data.remote.models.PokemonItem
import com.vivek.pokidex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryIml @Inject constructor(
    private val apiService: ApiService,
) : PokemonRepository {

    override fun fetchPokemonList(pageSize: Int): Flow<PagingData<PokemonItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(apiService) }
        ).flow
    }

//    override suspend fun getPokemonDetail(pokemonId: String): Pokemon? {
//        return database.pokemonDao().getPokemonById(pokemonId)?.toDomainModel()
//    }


}
