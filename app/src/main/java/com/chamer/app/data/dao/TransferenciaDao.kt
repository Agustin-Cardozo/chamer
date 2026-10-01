package com.chamer.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chamer.app.model.Transferencia
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferenciaDao {

    @Query("SELECT * FROM transferencias WHERE usuarioOrigenId = :usuarioId OR usuarioDestinoId = :usuarioId ORDER BY fecha DESC")
    fun getTransferenciasByUsuario(usuarioId: Long): Flow<List<Transferencia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransferencia(transferencia: Transferencia): Long

    @Query("DELETE FROM transferencias")
    suspend fun deleteAll(): Int
}
