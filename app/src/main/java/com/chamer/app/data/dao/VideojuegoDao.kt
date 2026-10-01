package com.chamer.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chamer.app.model.Videojuego
import kotlinx.coroutines.flow.Flow

@Dao
interface VideojuegoDao {

    @Query("SELECT * FROM videojuegos ORDER BY nombre ASC")
    fun getAllVideojuegos(): Flow<List<Videojuego>>

    @Query("SELECT * FROM videojuegos WHERE id = :id")
    suspend fun getVideojuegoById(id: Long): Videojuego?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideojuegos(videojuegos: List<Videojuego>): List<Long>

    @Query("DELETE FROM videojuegos")
    suspend fun deleteAll(): Int
}
