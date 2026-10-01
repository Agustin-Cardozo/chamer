package com.chamer.app.data.repository

import androidx.room.withTransaction
import com.chamer.app.data.database.AppDatabase
import com.chamer.app.model.Movimiento
import com.chamer.app.model.OfertaConVideojuego
import com.chamer.app.model.TipoMovimiento
import com.chamer.app.model.Transferencia
import com.chamer.app.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChamerRepository(private val db: AppDatabase) {

    val usuarios: Flow<List<Usuario>> = db.usuarioDao().getAllUsuarios()

    fun getUsuarioById(id: Long): Flow<Usuario?> = db.usuarioDao().getUsuarioById(id)

    fun getMovimientosByUsuario(usuarioId: Long): Flow<List<Movimiento>> =
        db.movimientoDao().getMovimientosByUsuario(usuarioId)

    fun getUltimosMovimientosByUsuario(usuarioId: Long, limit: Int = 5): Flow<List<Movimiento>> =
        db.movimientoDao().getUltimosMovimientosByUsuario(usuarioId, limit)

    fun getOfertasConVideojuego(): Flow<List<OfertaConVideojuego>> =
        db.ofertaDao().getOfertasConVideojuego()

    /**
     * Realiza una transferencia atómica de dinero en Room.
     * Busca al destinatario por Alias, CVU o Username.
     */
    suspend fun realizarTransferencia(
        usuarioOrigenId: Long,
        destinoInput: String,
        montoCents: Long,
        concepto: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        if (montoCents <= 0) {
            return@withContext Result.failure(IllegalArgumentException("El monto debe ser mayor a $0,00"))
        }

        val origen = db.usuarioDao().getUsuarioByIdDirect(usuarioOrigenId)
            ?: return@withContext Result.failure(IllegalArgumentException("Usuario de origen no encontrado"))

        val cleanedInput = destinoInput.trim().removePrefix("@")
        val destino = db.usuarioDao().getUsuarioByAliasCvuOrUsername(cleanedInput)
            ?: return@withContext Result.failure(IllegalArgumentException("No se encontró usuario para '$destinoInput'"))

        if (origen.id == destino.id) {
            return@withContext Result.failure(IllegalArgumentException("No puedes realizar una transferencia a ti mismo"))
        }

        if (origen.saldo < montoCents) {
            return@withContext Result.failure(IllegalArgumentException("Saldo insuficiente para realizar la transferencia"))
        }

        try {
            db.withTransaction {
                val nuevoSaldoOrigen = origen.saldo - montoCents
                val nuevoSaldoDestino = destino.saldo + montoCents

                db.usuarioDao().updateSaldo(origen.id, nuevoSaldoOrigen)
                db.usuarioDao().updateSaldo(destino.id, nuevoSaldoDestino)

                val conceptoFinal = concepto.ifBlank { "Transferencia de fondos" }

                val transferencia = Transferencia(
                    usuarioOrigenId = origen.id,
                    usuarioDestinoId = destino.id,
                    monto = montoCents,
                    concepto = conceptoFinal,
                    fecha = System.currentTimeMillis()
                )
                db.transferenciaDao().insertTransferencia(transferencia)

                val movOrigen = Movimiento(
                    usuarioId = origen.id,
                    tipo = TipoMovimiento.TRANSFERENCIA_ENVIADA,
                    descripcion = "Transferencia a ${destino.nombreCompleto} (@${destino.username})",
                    monto = montoCents,
                    fecha = System.currentTimeMillis()
                )

                val movDestino = Movimiento(
                    usuarioId = destino.id,
                    tipo = TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                    descripcion = "Transferencia de ${origen.nombreCompleto} (@${origen.username})",
                    monto = montoCents,
                    fecha = System.currentTimeMillis()
                )

                db.movimientoDao().insertMovimiento(movOrigen)
                db.movimientoDao().insertMovimiento(movDestino)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun ingresarDinero(
        usuarioId: Long,
        montoCents: Long,
        metodo: String = "Carga de saldo"
    ): Result<Unit> = withContext(Dispatchers.IO) {
        if (montoCents <= 0) {
            return@withContext Result.failure(IllegalArgumentException("El monto debe ser mayor a $0,00"))
        }

        val usuario = db.usuarioDao().getUsuarioByIdDirect(usuarioId)
            ?: return@withContext Result.failure(IllegalArgumentException("Usuario no encontrado"))

        try {
            db.withTransaction {
                val nuevoSaldo = usuario.saldo + montoCents
                db.usuarioDao().updateSaldo(usuario.id, nuevoSaldo)

                val movimiento = Movimiento(
                    usuarioId = usuario.id,
                    tipo = TipoMovimiento.INGRESO,
                    descripcion = "Carga de saldo ($metodo)",
                    monto = montoCents,
                    fecha = System.currentTimeMillis()
                )
                db.movimientoDao().insertMovimiento(movimiento)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun retirarDinero(
        usuarioId: Long,
        montoCents: Long,
        metodo: String = "Retiro a cuenta bancaria"
    ): Result<Unit> = withContext(Dispatchers.IO) {
        if (montoCents <= 0) {
            return@withContext Result.failure(IllegalArgumentException("El monto debe ser mayor a $0,00"))
        }

        val usuario = db.usuarioDao().getUsuarioByIdDirect(usuarioId)
            ?: return@withContext Result.failure(IllegalArgumentException("Usuario no encontrado"))

        if (usuario.saldo < montoCents) {
            return@withContext Result.failure(IllegalArgumentException("Saldo insuficiente para realizar el retiro"))
        }

        try {
            db.withTransaction {
                val nuevoSaldo = usuario.saldo - montoCents
                db.usuarioDao().updateSaldo(usuario.id, nuevoSaldo)

                val movimiento = Movimiento(
                    usuarioId = usuario.id,
                    tipo = TipoMovimiento.RETIRO,
                    descripcion = "Retiro de saldo ($metodo)",
                    monto = montoCents,
                    fecha = System.currentTimeMillis()
                )
                db.movimientoDao().insertMovimiento(movimiento)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun comprarOferta(
        usuarioId: Long,
        ofertaId: Long
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val usuario = db.usuarioDao().getUsuarioByIdDirect(usuarioId)
            ?: return@withContext Result.failure(IllegalArgumentException("Usuario no encontrado"))

        val ofertaConJuego = db.ofertaDao().getOfertaConVideojuegoById(ofertaId)
            ?: return@withContext Result.failure(IllegalArgumentException("La oferta no existe"))

        val precioOferta = ofertaConJuego.oferta.precioOferta

        if (usuario.saldo < precioOferta) {
            return@withContext Result.failure(IllegalArgumentException("Saldo insuficiente para comprar ${ofertaConJuego.videojuego.nombre}"))
        }

        try {
            db.withTransaction {
                val nuevoSaldo = usuario.saldo - precioOferta
                db.usuarioDao().updateSaldo(usuario.id, nuevoSaldo)

                val movimiento = Movimiento(
                    usuarioId = usuario.id,
                    tipo = TipoMovimiento.COMPRA,
                    descripcion = "Compra Gamer: ${ofertaConJuego.videojuego.nombre} (${ofertaConJuego.oferta.tienda})",
                    monto = precioOferta,
                    fecha = System.currentTimeMillis()
                )
                db.movimientoDao().insertMovimiento(movimiento)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetearBaseDeDatos(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            db.withTransaction {
                db.movimientoDao().deleteAll()
                db.transferenciaDao().deleteAll()
                db.ofertaDao().deleteAll()
                db.videojuegoDao().deleteAll()
                db.usuarioDao().deleteAll()

                AppDatabase.populateInitialData(db)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
