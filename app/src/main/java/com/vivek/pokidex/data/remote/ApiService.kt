package com.vivek.pokidex.data.remote

import com.vivek.pokidex.data.remote.models.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET(Endpoints.POKEMON)
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponse
}
