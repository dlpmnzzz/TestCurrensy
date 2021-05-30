package com.example.testcurrency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testcurrency.data.model.local.DbCurrency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: DbCurrency)

    @Query("SELECT * FROM currency WHERE name = :name")
    suspend fun getCurrency(name: String)
}