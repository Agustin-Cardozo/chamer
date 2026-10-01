package com.chamer.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chamer.app.model.Oferta
import com.chamer.app.model.OfertaConVideojuego
import kotlinx.coroutines.flow.Flow

@Dao
interface OfertaDao {

    @Transaction
    @Query("SELECT * FROM ofertas ORDER BY porcentajeDescuento DESC")
    fun getOfertasConVideojuego(): Flow<List<OfertaConVideojuego>>

    @Transaction
    @Query("SELECT * FROM ofertas WHERE id = :id")
    suspend fun getOfertaConVideojuegoById(id: Long): OfertaConVideojuego?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfertas(ofertas: List<Oferta>): List<Long>

    @Query("DELETE FROM ofertas")
    suspend fun deleteAll(): Int
}
