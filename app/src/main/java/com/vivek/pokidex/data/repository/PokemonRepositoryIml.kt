package com.vivek.pokidex.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vivek.pokidex.data.local.AppDatabase
import com.vivek.pokidex.data.remote.ApiService
import com.vivek.pokidex.data.remote.PokemonRemoteMediator
import com.vivek.pokidex.data.remote.models.toDomainModel
import com.vivek.pokidex.domain.model.Pokemon
import com.vivek.pokidex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryIml @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemon(): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokemonDao().getAllPokemon() }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = PokemonRemoteMediator(database, apiService),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }

    override suspend fun getPokemonDetail(pokemonId: String): Pokemon? {
        return database.pokemonDao().getPokemonById(pokemonId)?.toDomainModel()
    }


}
