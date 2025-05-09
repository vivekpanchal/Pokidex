package com.vivek.pokidex.data.remote.models

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json






@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "count")
    val count: Int,
    @Json(name = "results")
    val results: List<PokemonItem>
)

@JsonClass(generateAdapter = true)
data class PokemonItem(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
){
    val imageUrl: String
        get() {
            val id = url.split("/").dropLast(1).last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
}

