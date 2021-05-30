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

interface Repository {
    suspend fun getCurrency(name: String) : Flow<Result<Currency>>
}

class RepositoryImpl(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dbToUiMapper: DbToUiMapper,
    private val ApiToDbMapper: ApiToDbMapper
) : Repository {

    override suspend fun getCurrency(name: String): Flow<Result<Currency>> = flow {
        emit(Result.Loading)
        val oldLocalResult = local.getCurrency(name)
        emit(toUiResultOrLoading(oldLocalResult))

        if (oldLocalResult != null &&
            oldLocalResult.nextUpdateTime > System.currentTimeMillis()) {
            val networkCall = remote.getCurrency(name)
            if (networkCall.isSuccessful) {
                val body = networkCall.body()
                local.saveCurrency(ApiToDbMapper.map(body!!))
            } else {
                emit(Result.Error(Exception(networkCall.message())))
            }

            val localItem = getFromLocal(name)
            if (localItem is Result.Loading) {
                emit(Result.Error(Exception("Can't get currency for $name")))
            } else {
                emit(localItem)
            }
        }
    }

    private suspend fun getFromLocal(name: String): Result<Currency> {
        val dbSource = local.getCurrency(name) ?: return Result.Loading
        val item = dbToUiMapper.map(dbSource)
        return Result.Success(item)
    }

    private fun toUiResultOrLoading(currency: DbCurrency?) : Result<Currency>{
        return if (currency == null) {
            Result.Loading
        } else {
            Result.Success(dbToUiMapper.map(currency))
        }
    }
}
