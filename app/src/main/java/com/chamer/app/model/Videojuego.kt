package com.chamer.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videojuegos")
data class Videojuego(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String,
    val plataforma: String,
    val genero: String
)
