package com.vivek.pokidex.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vivek.pokidex.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY page ASC, name ASC")
    fun getAllPokemon(): PagingSource<Int, PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: String): PokemonEntity?

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
}