package com.chamer.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ofertas",
    foreignKeys = [
        ForeignKey(
            entity = Videojuego::class,
            parentColumns = ["id"],
            childColumns = ["videojuegoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["videojuegoId"])]
)
data class Oferta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videojuegoId: Long,
    val tienda: String, // Steam, Epic Games, PlayStation Store, Xbox, Nintendo
    val precioOriginal: Long, // Almacenado en centavos
    val precioOferta: Long, // Almacenado en centavos
    val porcentajeDescuento: Int,
    val urlOferta: String,
    val fechaInicio: Long = System.currentTimeMillis(),
    val fechaFin: Long = System.currentTimeMillis() + 864000000L // 10 días por defecto
)
