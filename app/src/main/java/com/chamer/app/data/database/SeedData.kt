package com.chamer.app.data.database

import com.chamer.app.model.Movimiento
import com.chamer.app.model.Oferta
import com.chamer.app.model.TipoMovimiento
import com.chamer.app.model.Usuario
import com.chamer.app.model.Videojuego

object SeedData {

    val usuariosPrueba = listOf(
        Usuario(
            id = 1L,
            nombre = "Agustín",
            apellido = "Pérez",
            username = "agustin",
            email = "agustin@chamer.app",
            saldo = 12545000L, // $125.450,00
            cvu = "0000003100011111111111",
            alias = "agustin.chamer.mp",
            direccion = "Av. Santa Fe 2450, CABA"
        ),
        Usuario(
            id = 2L,
            nombre = "Erick",
            apellido = "Gómez",
            username = "erick",
            email = "erick@chamer.app",
            saldo = 8500000L, // $85.000,00
            cvu = "0000003100022222222222",
            alias = "erick.chamer.mp",
            direccion = "Av. Cabildo 1820, CABA"
        ),
        Usuario(
            id = 3L,
            nombre = "Esteban",
            apellido = "Rodríguez",
            username = "esteban",
            email = "esteban@chamer.app",
            saldo = 21050000L, // $210.500,00
            cvu = "0000003100033333333333",
            alias = "esteban.chamer.mp",
            direccion = "Calle Florida 500, CABA"
        ),
        Usuario(
            id = 4L,
            nombre = "Sebastián",
            apellido = "López",
            username = "sebastian",
            email = "sebastian@chamer.app",
            saldo = 5000000L, // $50.000,00
            cvu = "0000003100044444444444",
            alias = "sebastian.chamer.mp",
            direccion = "Av. Rivadavia 3100, CABA"
        )
    )

    val videojuegosPrueba = listOf(
        Videojuego(
            id = 1L,
            nombre = "Cyberpunk 2077",
            descripcion = "Un RPG de acción y aventura ambientado en Night City.",
            imagenUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=600",
            plataforma = "PC / Consolas",
            genero = "Acción / RPG"
        ),
        Videojuego(
            id = 2L,
            nombre = "Elden Ring",
            descripcion = "El aclamado RPG de acción en un vasto mundo abierto.",
            imagenUrl = "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?q=80&w=600",
            plataforma = "PC / PlayStation / Xbox",
            genero = "Souls-like / RPG"
        ),
        Videojuego(
            id = 3L,
            nombre = "EA Sports FC 25",
            descripcion = "La experiencia de fútbol más auténtica e innovadora.",
            imagenUrl = "https://images.unsplash.com/photo-1511512578047-dfb367046420?q=80&w=600",
            plataforma = "Multiplataforma",
            genero = "Deportes"
        ),
        Videojuego(
            id = 4L,
            nombre = "Grand Theft Auto V",
            descripcion = "Vive la vida de tres criminales en Los Santos.",
            imagenUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?q=80&w=600",
            plataforma = "PC / Consolas",
            genero = "Acción / Mundo Abierto"
        ),
        Videojuego(
            id = 5L,
            nombre = "Hollow Knight",
            descripcion = "Una épica aventura a través de un reino de insectos arruinado.",
            imagenUrl = "https://images.unsplash.com/photo-1579373903781-fd5c0c30c4cd?q=80&w=600",
            plataforma = "PC / Nintendo Switch",
            genero = "Metroidvania"
        ),
        Videojuego(
            id = 6L,
            nombre = "God of War Ragnarök",
            descripcion = "Un viaje épico y emotivo con Kratos y Atreus.",
            imagenUrl = "https://images.unsplash.com/photo-1560253023-3ec5d502959f?q=80&w=600",
            plataforma = "PlayStation / PC",
            genero = "Acción / Aventura"
        )
    )

    val ofertasPrueba = listOf(
        Oferta(
            id = 1L,
            videojuegoId = 1L,
            tienda = "Steam",
            precioOriginal = 599900L,
            precioOferta = 299900L,
            porcentajeDescuento = 50,
            urlOferta = "https://store.steampowered.com"
        ),
        Oferta(
            id = 2L,
            videojuegoId = 2L,
            tienda = "Epic Games Store",
            precioOriginal = 699900L,
            precioOferta = 419900L,
            porcentajeDescuento = 40,
            urlOferta = "https://epicgames.com"
        ),
        Oferta(
            id = 3L,
            videojuegoId = 3L,
            tienda = "Epic Games Store",
            precioOriginal = 699900L,
            precioOferta = 349900L,
            porcentajeDescuento = 50,
            urlOferta = "https://epicgames.com"
        ),
        Oferta(
            id = 4L,
            videojuegoId = 4L,
            tienda = "Steam",
            precioOriginal = 299900L,
            precioOferta = 119900L,
            porcentajeDescuento = 60,
            urlOferta = "https://store.steampowered.com"
        ),
        Oferta(
            id = 5L,
            videojuegoId = 5L,
            tienda = "Nintendo eShop",
            precioOriginal = 149900L,
            precioOferta = 74900L,
            porcentajeDescuento = 50,
            urlOferta = "https://nintendo.com"
        ),
        Oferta(
            id = 6L,
            videojuegoId = 6L,
            tienda = "PlayStation Store",
            precioOriginal = 699900L,
            precioOferta = 489900L,
            porcentajeDescuento = 30,
            urlOferta = "https://store.playstation.com"
        )
    )

    fun getMovimientosIniciales(): List<Movimiento> {
        val now = System.currentTimeMillis()
        val day = 86400000L

        return listOf(
            Movimiento(
                usuarioId = 1L,
                tipo = TipoMovimiento.INGRESO,
                descripcion = "Carga de saldo inicial",
                monto = 10000000L,
                fecha = now - (3 * day)
            ),
            Movimiento(
                usuarioId = 1L,
                tipo = TipoMovimiento.INGRESO,
                descripcion = "Transferencia de @erick",
                monto = 2844900L,
                fecha = now - (2 * day)
            ),
            Movimiento(
                usuarioId = 1L,
                tipo = TipoMovimiento.COMPRA,
                descripcion = "Compra Steam - Cyberpunk 2077",
                monto = 299900L,
                fecha = now - (1 * day)
            ),
            Movimiento(
                usuarioId = 2L,
                tipo = TipoMovimiento.INGRESO,
                descripcion = "Carga inicial de saldo",
                monto = 8500000L,
                fecha = now - (4 * day)
            ),
            Movimiento(
                usuarioId = 3L,
                tipo = TipoMovimiento.INGRESO,
                descripcion = "Depósito bancario inicial",
                monto = 21050000L,
                fecha = now - (5 * day)
            ),
            Movimiento(
                usuarioId = 4L,
                tipo = TipoMovimiento.INGRESO,
                descripcion = "Transferencia de bienvenida",
                monto = 5000000L,
                fecha = now - (2 * day)
            )
        )
    }
}
