package com.example.testcurrency.data.remote

import com.example.testcurrency.data.model.remote.CurrencyResponseEntity
import com.example.testcurrency.data.remote.api.CurrencyService
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCurrency(name: String): Response<CurrencyResponseEntity>
}

class RemoteDataSourceImp(private val api: CurrencyService) : RemoteDataSource {
    override suspend fun getCurrency(name: String): Response<CurrencyResponseEntity> {
        return api.getCurrency(name)
    }

}