package com.example.testcurrency.data

import com.example.testcurrency.data.local.LocalDataSource
import com.example.testcurrency.data.mappers.ApiToDbMapper
import com.example.testcurrency.data.mappers.DbToDomainMapper
import com.example.testcurrency.data.model.Currency
import com.example.testcurrency.data.model.local.DbCurrency
import com.example.testcurrency.data.model.remote.CurrencyResponseEntity
import com.example.testcurrency.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface CurrencyRepository {
    fun getCurrency(name: String): Flow<Currency>
}

class RepositoryImpl(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dbToDomainMapper: DbToDomainMapper,
    private val ApiToDbMapper: ApiToDbMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyRepository {

    override fun getCurrency(name: String): Flow<Currency> = flow {
        val oldLocalResult = local.getCurrency(name)
        oldLocalResult?.let { currency ->
            emit(currency.toDomain())
        }

        if (oldLocalResult == null ||
            oldLocalResult.nextUpdateTime > System.currentTimeMillis()
        ) {
            val networkCall = remote.getCurrency(name)
            if (networkCall.isSuccessful) {
                val body = networkCall.body()
                saveToDb(body)
            } else {
                error(networkCall.message())
            }

            val item = getFromLocal(name)
            emit(item)
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveToDb(currencyResponseEntity: CurrencyResponseEntity?) {
        currencyResponseEntity?.let {
            local.saveCurrency(it.toDb())
        } ?: error("empty body from response")
    }

    private suspend fun getFromLocal(name: String): Currency {
        val dbCurrency = local.getCurrency(name) ?: error("not found in db")
        return dbCurrency.toDomain()
    }

    private fun DbCurrency.toDomain(): Currency {
        return dbToDomainMapper.map(this)
    }

    private fun CurrencyResponseEntity.toDb(): DbCurrency {
        return ApiToDbMapper.map(this)
    }
}
