package com.chamer.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chamer.app.data.dao.MovimientoDao
import com.chamer.app.data.dao.OfertaDao
import com.chamer.app.data.dao.TransferenciaDao
import com.chamer.app.data.dao.UsuarioDao
import com.chamer.app.data.dao.VideojuegoDao
import com.chamer.app.model.Movimiento
import com.chamer.app.model.Oferta
import com.chamer.app.model.Transferencia
import com.chamer.app.model.Usuario
import com.chamer.app.model.Videojuego
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class,
        Movimiento::class,
        Transferencia::class,
        Videojuego::class,
        Oferta::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun movimientoDao(): MovimientoDao
    abstract fun transferenciaDao(): TransferenciaDao
    abstract fun videojuegoDao(): VideojuegoDao
    abstract fun ofertaDao(): OfertaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chamer_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Precargar datos iniciales al crear la BD por primera vez
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateInitialData(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            database.usuarioDao().insertUsuarios(SeedData.usuariosPrueba)
            database.videojuegoDao().insertVideojuegos(SeedData.videojuegosPrueba)
            database.ofertaDao().insertOfertas(SeedData.ofertasPrueba)
            database.movimientoDao().insertMovimientos(SeedData.getMovimientosIniciales())
        }
    }
}
