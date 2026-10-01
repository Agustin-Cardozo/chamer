package com.chamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chamer.app.data.database.AppDatabase
import com.chamer.app.data.repository.ChamerRepository
import com.chamer.app.model.Movimiento
import com.chamer.app.model.OfertaConVideojuego
import com.chamer.app.model.Usuario
import com.chamer.app.ui.theme.AppThemeMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChamerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChamerRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = ChamerRepository(db)
    }

    // Lista de usuarios registrados
    val usuarios: StateFlow<List<Usuario>> = repository.usuarios.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ID del usuario activo en la app (por defecto Agustín = 1L)
    private val _usuarioActivoId = MutableStateFlow<Long>(1L)
    val usuarioActivoId: StateFlow<Long> = _usuarioActivoId.asStateFlow()

    // Usuario activo actual
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivoId.flatMapLatest { id ->
        repository.getUsuarioById(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Movimientos del usuario activo
    val movimientos: StateFlow<List<Movimiento>> = _usuarioActivoId.flatMapLatest { id ->
        repository.getMovimientosByUsuario(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Últimos 5 movimientos para el inicio
    val ultimosMovimientos: StateFlow<List<Movimiento>> = _usuarioActivoId.flatMapLatest { id ->
        repository.getUltimosMovimientosByUsuario(id, limit = 5)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Ofertas de videojuegos disponibles
    val ofertasConVideojuego: StateFlow<List<OfertaConVideojuego>> = repository.getOfertasConVideojuego().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Modo de tema (Sistema, Claro, Oscuro)
    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    // Mensajes para el usuario (Snackbar / Toast)
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun seleccionarUsuarioActivo(id: Long) {
        _usuarioActivoId.value = id
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun realizarTransferencia(
        destinoUsername: String,
        montoCents: Long,
        concepto: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = repository.realizarTransferencia(
                usuarioOrigenId = _usuarioActivoId.value,
                destinoInput = destinoUsername,
                montoCents = montoCents,
                concepto = concepto
            )
            result.onSuccess {
                _userMessage.value = "¡Transferencia realizada con éxito!"
                onSuccess()
            }.onFailure { error ->
                _userMessage.value = error.message ?: "Error al realizar la transferencia"
            }
        }
    }

    fun ingresarDinero(montoCents: Long, metodo: String = "Depósito", onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = repository.ingresarDinero(_usuarioActivoId.value, montoCents, metodo)
            result.onSuccess {
                _userMessage.value = "¡Ingreso de dinero exitoso!"
                onSuccess()
            }.onFailure { error ->
                _userMessage.value = error.message ?: "Error al ingresar dinero"
            }
        }
    }

    fun retirarDinero(montoCents: Long, metodo: String = "Retiro bancario", onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = repository.retirarDinero(_usuarioActivoId.value, montoCents, metodo)
            result.onSuccess {
                _userMessage.value = "¡Retiro de fondos exitoso!"
                onSuccess()
            }.onFailure { error ->
                _userMessage.value = error.message ?: "Error al retirar fondos"
            }
        }
    }

    fun comprarOferta(ofertaId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = repository.comprarOferta(_usuarioActivoId.value, ofertaId)
            result.onSuccess {
                _userMessage.value = "¡Juego comprado con éxito! Revisa tu historial."
                onSuccess()
            }.onFailure { error ->
                _userMessage.value = error.message ?: "Error al realizar la compra"
            }
        }
    }

    fun resetearBaseDeDatos() {
        viewModelScope.launch {
            val result = repository.resetearBaseDeDatos()
            result.onSuccess {
                _usuarioActivoId.value = 1L
                _userMessage.value = "Base de datos restablecida al estado inicial"
            }.onFailure { error ->
                _userMessage.value = "Error al restablecer base de datos: ${error.message}"
            }
        }
    }
}
