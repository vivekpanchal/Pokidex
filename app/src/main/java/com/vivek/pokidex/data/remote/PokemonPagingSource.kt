package com.vivek.pokidex.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vivek.pokidex.data.remote.models.PokemonItem

class PokemonPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, PokemonItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonItem> {
        return try {
            val currentPage = params.key ?: 0  // Start with page 0
            val limit = 40                      // Fetch 20 Pokémon per request
            val offset = currentPage * limit    // Calculate offset
            val response = apiService.getPokemonList(limit, offset)
            val pokemonList = response.results
            LoadResult.Page(
                data = pokemonList,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (pokemonList.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
