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
        emit(getFromLocal(name))

        val networkCall = remote.getCurrency(name)
        if (networkCall.isSuccessful) {
            val body = networkCall.body()
            local.saveCurrency(ApiToDbMapper.map(body!!))
            emit(getFromLocal(name))
        } else {
            emit(Result.Error(Exception(networkCall.message())))
        }
    }

    private suspend fun getFromLocal(name: String): Result.Success<Currency> {
        val dbSource = local.getCurrency(name)
        val item = dbToUiMapper.map(dbSource)
        return Result.Success(item)
    }


    val cr = Currency(
        "USD", hashMapOf(
            "USD" to 1f,
            "EUR" to 0.82f,
            "RUB" to 73.41f,
        ))

}

private fun DbCurrency.toUiEnity(): Result.Loading {
    TODO("Not yet implemented")
}
