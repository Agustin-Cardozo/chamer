package com.chamer.app.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong

object CurrencyUtils {

    /**
     * Formatea una cantidad dada en centavos a un String de moneda legible.
     * Ejemplo: 12545000L -> "$ 125.450,00"
     */
    fun formatCurrency(cents: Long, includeSymbol: Boolean = true): String {
        val dollars = cents / 100
        val remainder = abs(cents % 100)
        val numberFormat = NumberFormat.getNumberInstance(Locale("es", "AR"))
        val formattedWhole = numberFormat.format(dollars)
        val formattedCents = remainder.toString().padStart(2, '0')
        val amountStr = "$formattedWhole,$formattedCents"
        return if (includeSymbol) "$ $amountStr" else amountStr
    }

    /**
     * Convierte un texto ingresado por el usuario (ej: "1250", "1.250,50", "1250.50")
     * a su valor equivalente en centavos de tipo Long.
     */
    fun parseAmountToCents(input: String): Long? {
        val clean = input.trim().replace("$", "").replace(" ", "")
        if (clean.isEmpty()) return null

        return try {
            val normalized = when {
                clean.contains(",") && clean.contains(".") -> {
                    if (clean.indexOf(",") > clean.indexOf(".")) {
                        clean.replace(".", "").replace(",", ".")
                    } else {
                        clean.replace(",", "")
                    }
                }
                clean.contains(",") -> clean.replace(",", ".")
                else -> clean
            }
            val doubleVal = normalized.toDoubleOrNull() ?: return null
            if (doubleVal <= 0) return null
            (doubleVal * 100).roundToLong()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formatea un timestamp a fecha legible (ej: "18 Mayo 2025, 14:30")
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "ES"))
        return sdf.format(Date(timestamp))
    }
}
