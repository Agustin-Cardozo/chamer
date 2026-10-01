package com.chamer.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.chamer.app.ui.components.BalanceCard
import com.chamer.app.ui.components.DepositWithdrawDialog
import com.chamer.app.ui.components.MovementItem
import com.chamer.app.ui.components.OfferCard
import com.chamer.app.ui.components.QuickActionButton
import com.chamer.app.ui.components.ReceiveMoneyDialog
import com.chamer.app.ui.components.UserDropdownSelector
import com.chamer.app.viewmodel.ChamerViewModel

@Composable
fun HomeScreen(
    viewModel: ChamerViewModel,
    onNavigateToWallet: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToGaming: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuarios by viewModel.usuarios.collectAsState()
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()
    val ultimosMovimientos by viewModel.ultimosMovimientos.collectAsState()
    val ofertasConVideojuego by viewModel.ofertasConVideojuego.collectAsState()

    var showDepositDialog by remember { mutableStateOf(false) }
    var showReceiveDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Cabecera superior: Logo CHAMER y Selector de Usuario
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CHAMER",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            UserDropdownSelector(
                usuarios = usuarios,
                usuarioActivo = usuarioActivo,
                onSelectUsuario = { id -> viewModel.seleccionarUsuarioActivo(id) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta principal de Saldo con CVU y Alias
        BalanceCard(usuario = usuarioActivo)

        Spacer(modifier = Modifier.height(20.dp))

        // Botones de Acciones Rápida Grandes (Enviar dinero, Recibir dinero, Cargar saldo)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickActionButton(
                    icon = Icons.AutoMirrored.Filled.Send,
                    label = "Enviar dinero",
                    onClick = onNavigateToTransfer
                )
                QuickActionButton(
                    icon = Icons.Default.QrCode2,
                    label = "Recibir dinero",
                    onClick = { showReceiveDialog = true }
                )
                QuickActionButton(
                    icon = Icons.Default.AddCard,
                    label = "Cargar saldo",
                    onClick = { showDepositDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección: Últimos Movimientos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Últimos movimientos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onNavigateToWallet) {
                Text(text = "Ver más")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (ultimosMovimientos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Aún no tienes movimientos registrados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            ultimosMovimientos.forEach { movimiento ->
                MovementItem(movimiento = movimiento)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección: Ofertas Gamer Destacadas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ofertas Gamer Destacadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(onClick = onNavigateToGaming) {
                Text(text = "Ver catálogo")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ofertasConVideojuego.take(2).forEach { ofertaConJuego ->
            OfferCard(
                ofertaConJuego = ofertaConJuego,
                onComprarClick = { ofertaId ->
                    viewModel.comprarOferta(ofertaId)
                }
            )
        }
    }

    // Diálogos emergentes
    if (showDepositDialog) {
        DepositWithdrawDialog(
            isDeposit = true,
            onDismiss = { showDepositDialog = false },
            onConfirm = { monto ->
                viewModel.ingresarDinero(monto)
                showDepositDialog = false
            }
        )
    }

    if (showReceiveDialog) {
        ReceiveMoneyDialog(
            usuario = usuarioActivo,
            onDismiss = { showReceiveDialog = false }
        )
    }
}
