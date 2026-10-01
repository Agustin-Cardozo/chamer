package com.chamer.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chamer.app.ui.theme.ExpenseRed
import com.chamer.app.ui.theme.IncomeGreen
import com.chamer.app.ui.theme.PrimaryGreen
import com.chamer.app.util.CurrencyUtils
import com.chamer.app.viewmodel.ChamerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransferScreen(
    viewModel: ChamerViewModel,
    modifier: Modifier = Modifier
) {
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()
    val usuarios by viewModel.usuarios.collectAsState()

    var recipientInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var conceptInput by remember { mutableStateOf("") }
    var showUserDropdown by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Contactos sugeridos (excluyendo al usuario activo)
    val contactOptions = remember(usuarios, usuarioActivo) {
        usuarios.filter { it.id != usuarioActivo?.id }
    }

    // Validación visual por Alias, CVU o Username
    val cleanedInput = recipientInput.trim().removePrefix("@")
    val matchedRecipientUser = contactOptions.find {
        it.username.equals(cleanedInput, ignoreCase = true) ||
                it.alias.equals(cleanedInput, ignoreCase = true) ||
                it.cvu == cleanedInput
    }

    val montoCents = CurrencyUtils.parseAmountToCents(amountInput) ?: 0L
    val saldoActual = usuarioActivo?.saldo ?: 0L

    val isSelfTransfer = usuarioActivo?.let {
        it.username.equals(cleanedInput, ignoreCase = true) ||
                it.alias.equals(cleanedInput, ignoreCase = true) ||
                it.cvu == cleanedInput
    } == true

    val isRecipientValid = matchedRecipientUser != null
    val isAmountValid = montoCents > 0 && montoCents <= saldoActual
    val canSubmit = isRecipientValid && !isSelfTransfer && isAmountValid

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Transferencias",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de Origen / Saldo Disponible
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Origen: ${usuarioActivo?.nombreCompleto ?: ""}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Saldo disponible: ${CurrencyUtils.formatCurrency(saldoActual)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 1: Buscar por Alias, CVU o @username
        Column {
            Text(
                text = "Buscar por Alias, CVU o Username",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = recipientInput,
                onValueChange = {
                    recipientInput = it
                    showUserDropdown = true
                },
                placeholder = { Text("ej: erick.chamer.mp, @erick o CVU") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (isRecipientValid) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Destinatario encontrado",
                            tint = IncomeGreen
                        )
                    } else if (recipientInput.isNotBlank() && (isSelfTransfer || !isRecipientValid)) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Destinatario no encontrado",
                            tint = ExpenseRed
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Menú desplegable de sugerencias de contactos
            DropdownMenu(
                expanded = showUserDropdown && contactOptions.isNotEmpty() && recipientInput.isBlank(),
                onDismissRequest = { showUserDropdown = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Seleccionar destinatario de prueba:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
                contactOptions.forEach { user ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(user.nombreCompleto, fontWeight = FontWeight.Bold)
                                Text("Alias: ${user.alias}", style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        onClick = {
                            recipientInput = user.alias
                            showUserDropdown = false
                        }
                    )
                }
            }

            // Chips rápidos de contactos
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                contactOptions.forEach { user ->
                    SuggestionChip(
                        onClick = { recipientInput = user.alias },
                        label = { Text(user.alias) }
                    )
                }
            }

            // Mensajes de error visuales
            if (isSelfTransfer) {
                Text(
                    text = "⚠️ No puedes realizar una transferencia a tu propia cuenta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ExpenseRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else if (recipientInput.isNotBlank() && !isRecipientValid) {
                Text(
                    text = "⚠️ No se encontró ningún destinatario registrado con '$recipientInput'",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ExpenseRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 2: Selector de Monto
        Column {
            Text(
                text = "Monto a Transferir",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = amountInput,
                onValueChange = { amountInput = it },
                placeholder = { Text("0,00") },
                prefix = { Text("$ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = amountInput.isNotBlank() && !isAmountValid,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Chips rápidos (+1.000, +5.000, +10.000, +50.000)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1000L, 5000L, 10000L, 50000L).forEach { quickAmount ->
                    SuggestionChip(
                        onClick = {
                            val currentCents = CurrencyUtils.parseAmountToCents(amountInput) ?: 0L
                            val newCents = currentCents + (quickAmount * 100)
                            amountInput = (newCents / 100).toString()
                        },
                        label = { Text("+$${quickAmount}") }
                    )
                }
            }

            if (montoCents > saldoActual) {
                Text(
                    text = "⚠️ Saldo insuficiente (${CurrencyUtils.formatCurrency(saldoActual)})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ExpenseRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 3: Concepto
        Column {
            Text(
                text = "Concepto / Motivo",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = conceptInput,
                onValueChange = { conceptInput = it },
                placeholder = { Text("ej: Varios, Pago de juego, Regalo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botón verde #2E7D32 "Transferir"
        Button(
            onClick = { showConfirmDialog = true },
            enabled = canSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Transferir",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Diálogo de Confirmación de Operación
    if (showConfirmDialog && matchedRecipientUser != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(text = "Confirmar Transferencia", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Estás a punto de enviar dinero:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Destinatario:",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "${matchedRecipientUser.nombreCompleto} (${matchedRecipientUser.alias})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Monto:",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = CurrencyUtils.formatCurrency(montoCents),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryGreen
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Concepto: ${conceptInput.ifBlank { "Varios" }}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.realizarTransferencia(
                            destinoUsername = recipientInput,
                            montoCents = montoCents,
                            concepto = conceptInput,
                            onSuccess = {
                                recipientInput = ""
                                amountInput = ""
                                conceptInput = ""
                            }
                        )
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(text = "Confirmar Envío")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
