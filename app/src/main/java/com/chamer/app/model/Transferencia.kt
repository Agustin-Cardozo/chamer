package com.chamer.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

object EstadoTransferencia {
    const val PENDIENTE = "PENDIENTE"
    const val COMPLETADA = "COMPLETADA"
    const val CANCELADA = "CANCELADA"
}

@Entity(
    tableName = "transferencias",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioOrigenId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioDestinoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["usuarioOrigenId"]),
        Index(value = ["usuarioDestinoId"])
    ]
)
data class Transferencia(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioOrigenId: Long,
    val usuarioDestinoId: Long,
    val monto: Long, // Almacenado en centavos
    val concepto: String,
    val fecha: Long = System.currentTimeMillis(),
    val estado: String = EstadoTransferencia.COMPLETADA
)
