package com.example.testcurrency.data

import com.example.testcurrency.data.local.LocalDataSource
import com.example.testcurrency.data.mappers.ApiToDbMapper
import com.example.testcurrency.data.mappers.DbToUiMapper
import com.example.testcurrency.data.model.Currency
import com.example.testcurrency.data.model.local.DbCurrency
import com.example.testcurrency.data.remote.RemoteDataSource
import com.example.testcurrency.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

interface CurrencyRepository {
    fun getCurrency(name: String): Flow<Currency>
}

class RepositoryImpl(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dbToUiMapper: DbToUiMapper,
    private val ApiToDbMapper: ApiToDbMapper
) : CurrencyRepository {

    override fun getCurrency(name: String): Flow<Currency> = flow {
        val oldLocalResult = local.getCurrency(name)
        emit(toUiResultOrLoading(oldLocalResult))

        if (oldLocalResult == null ||
            oldLocalResult.nextUpdateTime > System.currentTimeMillis()
        ) {
            val networkCall = remote.getCurrency(name)
            if (networkCall.isSuccessful) {
                val body = networkCall.body()
                local.saveCurrency(ApiToDbMapper.map(body!!))
            } else {
                error(networkCall.message())
            }

            val item = getFromLocal(name)
            emit(item)
        }
    }

    private suspend fun getFromLocal(name: String): Currency {
        val dbSource = local.getCurrency(name) ?: error("not found in db")
        return dbToUiMapper.map(dbSource)
    }

    private fun toUiResultOrLoading(currency: DbCurrency?): Currency {
        if (currency == null) {
            error("")
        }
        return dbToUiMapper.map(currency)
    }
}
