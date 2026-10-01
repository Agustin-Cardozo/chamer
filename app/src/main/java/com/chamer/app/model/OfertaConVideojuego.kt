package com.chamer.app.model

import androidx.room.Embedded
import androidx.room.Relation

data class OfertaConVideojuego(
    @Embedded val oferta: Oferta,
    @Relation(
        parentColumn = "videojuegoId",
        entityColumn = "id"
    )
    val videojuego: Videojuego
)
