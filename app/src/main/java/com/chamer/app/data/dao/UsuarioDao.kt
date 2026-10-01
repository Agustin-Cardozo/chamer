package com.chamer.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chamer.app.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    fun getAllUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    suspend fun getAllUsuariosList(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: Long): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioByIdDirect(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE LOWER(username) = LOWER(:username) OR LOWER(username) = LOWER(:cleanedUsername) LIMIT 1")
    suspend fun getUsuarioByUsername(username: String, cleanedUsername: String = username.removePrefix("@")): Usuario?

    @Query("SELECT * FROM usuarios WHERE LOWER(username) = LOWER(:input) OR LOWER(username) = LOWER(:cleanedInput) OR LOWER(alias) = LOWER(:input) OR cvu = :input LIMIT 1")
    suspend fun getUsuarioByAliasCvuOrUsername(input: String, cleanedInput: String = input.removePrefix("@")): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarios(usuarios: List<Usuario>): List<Long>

    @Update
    suspend fun updateUsuario(usuario: Usuario): Int

    @Query("UPDATE usuarios SET saldo = :nuevoSaldo WHERE id = :id")
    suspend fun updateSaldo(id: Long, nuevoSaldo: Long): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun getCount(): Int

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll(): Int
}
