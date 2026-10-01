package com.chamer.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chamer.app.model.TipoMovimiento
import com.chamer.app.ui.components.MovementItem
import com.chamer.app.ui.theme.PrimaryGreen
import com.chamer.app.util.CurrencyUtils
import com.chamer.app.viewmodel.ChamerViewModel

enum class MovimientoFilter(val label: String) {
    TODOS("Todos"),
    INGRESOS("Ingresos"),
    RETIROS("Retiros"),
    TRANSFERENCIAS_ENVIADAS("Enviadas"),
    TRANSFERENCIAS_RECIBIDAS("Recibidas"),
    COMPRAS("Compras Gamer")
}

@Composable
fun WalletScreen(
    viewModel: ChamerViewModel,
    modifier: Modifier = Modifier
) {
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()
    val movimientos by viewModel.movimientos.collectAsState()

    var selectedFilter by remember { mutableStateOf(MovimientoFilter.TODOS) }

    val filteredMovimientos = remember(movimientos, selectedFilter) {
        when (selectedFilter) {
            MovimientoFilter.TODOS -> movimientos
            MovimientoFilter.INGRESOS -> movimientos.filter { it.tipo == TipoMovimiento.INGRESO }
            MovimientoFilter.RETIROS -> movimientos.filter { it.tipo == TipoMovimiento.RETIRO }
            MovimientoFilter.TRANSFERENCIAS_ENVIADAS -> movimientos.filter { it.tipo == TipoMovimiento.TRANSFERENCIA_ENVIADA }
            MovimientoFilter.TRANSFERENCIAS_RECIBIDAS -> movimientos.filter { it.tipo == TipoMovimiento.TRANSFERENCIA_RECIBIDA }
            MovimientoFilter.COMPRAS -> movimientos.filter { it.tipo == TipoMovimiento.COMPRA }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        // Título de la Pantalla y Saldo Resumido
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Historial de Billetera",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = usuarioActivo?.usernameFormateado ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Saldo Actual",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = CurrencyUtils.formatCurrency(usuarioActivo?.saldo ?: 0L),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Filtrar por tipo:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chips de Filtro
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(MovimientoFilter.entries.toTypedArray()) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(text = filter.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryGreen,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de Movimientos
        if (filteredMovimientos.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No hay movimientos para este filtro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredMovimientos, key = { it.id }) { movimiento ->
                    MovementItem(movimiento = movimiento)
                }
            }
        }
    }
}
