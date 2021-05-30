package com.example.testcurrency.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testcurrency.data.model.local.DbCurrency

@Database(entities = [DbCurrency::class], version = 1, exportSchema = false)
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
                .build()
    }
}