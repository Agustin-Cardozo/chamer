package com.chamer.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

object TipoMovimiento {
    const val INGRESO = "INGRESO"
    const val RETIRO = "RETIRO"
    const val TRANSFERENCIA_ENVIADA = "TRANSFERENCIA_ENVIADA"
    const val TRANSFERENCIA_RECIBIDA = "TRANSFERENCIA_RECIBIDA"
    const val COMPRA = "COMPRA"
}

@Entity(
    tableName = "movimientos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioId"])]
)
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long,
    val tipo: String, // INGRESO, RETIRO, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECIBIDA, COMPRA
    val descripcion: String,
    val monto: Long, // Almacenado en centavos
    val fecha: Long = System.currentTimeMillis(),
    val estado: String = "COMPLETADO"
)
