package com.example.testcurrency.data.local

import com.example.testcurrency.data.model.local.DbCurrency

interface LocalDataSource {
    suspend fun getCurrency(name: String): DbCurrency?
    suspend fun saveCurrency(item: DbCurrency)
}

class LocalDataSourceImp(private val db: AppDatabase) : LocalDataSource {
    override suspend fun getCurrency(name: String): DbCurrency? {
        return db.currencyDao().getCurrency(name)
    }

    override suspend fun saveCurrency(item: DbCurrency) {
        db.currencyDao().insert(item)
    }

}