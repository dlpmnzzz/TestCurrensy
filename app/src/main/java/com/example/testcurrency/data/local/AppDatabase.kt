package com.example.testcurrency.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.testcurrency.data.model.local.DbCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@Database(entities = [], version = 1, exportSchema = false)
@TypeConverters(MapConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao() : CurrencyDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "currency")
                .fallbackToDestructiveMigration()
                .addCallback(object :  RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            val currency = DbCurrency("USD", hashMapOf("USD" to 1f,
                                "EUR" to 0.82f,
                                "RUB" to 73.41f,))
                            val currency1 = DbCurrency("EUR", hashMapOf("USD" to 1f,
                                "USD" to 1.22f,
                                "RUB" to 89.45f,))
                            val dao = getDatabase(appContext).currencyDao()
                            dao.insert(currency)
                            dao.insert(currency1)
                        }
                    }
                })
                .build()
    }
}