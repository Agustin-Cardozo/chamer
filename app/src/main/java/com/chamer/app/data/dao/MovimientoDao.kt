package com.chamer.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chamer.app.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Query("SELECT * FROM movimientos WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun getMovimientosByUsuario(usuarioId: Long): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE usuarioId = :usuarioId ORDER BY fecha DESC LIMIT :limit")
    fun getUltimosMovimientosByUsuario(usuarioId: Long, limit: Int = 5): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos ORDER BY fecha DESC")
    fun getAllMovimientos(): Flow<List<Movimiento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimiento(movimiento: Movimiento): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovimientos(movimientos: List<Movimiento>): List<Long>

    @Query("DELETE FROM movimientos")
    suspend fun deleteAll(): Int
}
