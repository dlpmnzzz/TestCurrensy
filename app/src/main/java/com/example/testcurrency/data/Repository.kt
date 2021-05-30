package com.example.testcurrency.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.testcurrency.data.local.LocalDataSource
import com.example.testcurrency.data.mappers.DbToUiMapper
import com.example.testcurrency.data.model.Currency
import com.example.testcurrency.data.model.local.DbCurrency
import com.example.testcurrency.data.remote.RemoteDataSource
import com.example.testcurrency.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

interface Repository {
    suspend fun getCurrency(name: String) : Flow<Result<Currency>>
}

class RepositoryImpl(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dbToUiMapper: DbToUiMapper
) : Repository {

    override suspend fun getCurrency(name: String): Flow<Result<Currency>> = flow {
        emit(Result.Loading)
        val dbSource = local.getCurrency(name)
        val item = dbToUiMapper.map(dbSource)
        emit(Result.Success(item))
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
