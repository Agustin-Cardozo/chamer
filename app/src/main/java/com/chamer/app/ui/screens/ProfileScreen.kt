package com.chamer.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chamer.app.ui.theme.AppThemeMode
import com.chamer.app.ui.theme.ExpenseRed
import com.chamer.app.ui.theme.PrimaryGreen
import com.chamer.app.viewmodel.ChamerViewModel

@Composable
fun ProfileScreen(
    viewModel: ChamerViewModel,
    modifier: Modifier = Modifier
) {
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val context = LocalContext.current

    var showResetDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var isBiometricEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Perfil y Configuración",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de Perfil Principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = usuarioActivo?.nombreCompleto ?: "Usuario CHAMER",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = usuarioActivo?.usernameFormateado ?: "@usuario",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección: Datos Personales FinTech
        Text(
            text = "Datos Personales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileDetailItem(
                    icon = Icons.Default.Email,
                    label = "Email registrado",
                    value = usuarioActivo?.email ?: ""
                )
                ProfileDetailItem(
                    icon = Icons.Default.Badge,
                    label = "Alias de Billetera",
                    value = usuarioActivo?.alias ?: "",
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Alias", usuarioActivo?.alias ?: ""))
                        Toast.makeText(context, "Alias copiado", Toast.LENGTH_SHORT).show()
                    }
                )
                ProfileDetailItem(
                    icon = Icons.Default.Badge,
                    label = "CVU",
                    value = usuarioActivo?.cvu ?: "",
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("CVU", usuarioActivo?.cvu ?: ""))
                        Toast.makeText(context, "CVU copiado", Toast.LENGTH_SHORT).show()
                    }
                )
                ProfileDetailItem(
                    icon = Icons.Default.Home,
                    label = "Dirección declarada",
                    value = usuarioActivo?.direccion ?: ""
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección: Seguridad
        Text(
            text = "Seguridad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Opción Cambiar Contraseña
                Surface(
                    onClick = { showPasswordDialog = true },
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Cambiar contraseña", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Opción Biometría
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = PrimaryGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Ingreso con Huella / Biometría", style = MaterialTheme.typography.bodyLarge)
                    }
                    Switch(
                        checked = isBiometricEnabled,
                        onCheckedChange = { isBiometricEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección: Apariencia y Tema
        Text(
            text = "Tema Visual",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ThemeOptionItem(
                    title = "Modo Claro ☀️",
                    icon = Icons.Default.Brightness7,
                    selected = themeMode == AppThemeMode.LIGHT,
                    onClick = { viewModel.setThemeMode(AppThemeMode.LIGHT) }
                )
                ThemeOptionItem(
                    title = "Modo Oscuro 🌙",
                    icon = Icons.Default.Brightness4,
                    selected = themeMode == AppThemeMode.DARK,
                    onClick = { viewModel.setThemeMode(AppThemeMode.DARK) }
                )
                ThemeOptionItem(
                    title = "Tema del Sistema ⚙️",
                    icon = Icons.Default.SettingsSuggest,
                    selected = themeMode == AppThemeMode.SYSTEM,
                    onClick = { viewModel.setThemeMode(AppThemeMode.SYSTEM) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sección: Información Académica
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = PrimaryGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Proyecto Académico CHAMER v1.0", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Billetera Virtual con Enfoque Gamer desarrollada con MVVM, Room ORM, Jetpack Compose y Material 3.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Restablecer Datos de Prueba
        OutlinedButton(
            onClick = { showResetDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Restablecer datos de prueba", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Cerrar Sesión
        Button(
            onClick = {
                Toast.makeText(context, "Sesión de ${usuarioActivo?.nombre} pausada", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cerrar sesión", fontWeight = FontWeight.Bold)
        }
    }

    // Diálogo Cambiar Contraseña
    if (showPasswordDialog) {
        var newPass by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Cambiar Contraseña", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Ingresa tu nueva contraseña para CHAMER Wallet:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("Nueva Contraseña") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show()
                        showPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo Restablecer Datos de Prueba
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(text = "Restablecer Datos de Prueba", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "¿Deseas restablecer la base de datos a sus valores iniciales de prueba? " +
                            "Se restaurarán los saldos originales de Agustín ($125.450,00), Erick, Esteban y Sebastián."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetearBaseDeDatos()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                ) {
                    Text("Sí, restablecer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProfileDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onCopy: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
        if (onCopy != null) {
            IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar", modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun ThemeOptionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}
