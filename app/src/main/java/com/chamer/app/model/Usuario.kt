package com.chamer.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val apellido: String,
    val username: String,
    val email: String,
    val saldo: Long, // Saldo almacenado en centavos
    val cvu: String = "0000003100012345678901",
    val alias: String = "usuario.chamer.mp",
    val direccion: String = "Av. Corrientes 1234, CABA",
    val fechaCreacion: Long = System.currentTimeMillis()
) {
    val nombreCompleto: String
        get() = "$nombre $apellido"

    val usernameFormateado: String
        get() = if (username.startsWith("@")) username else "@$username"
}
