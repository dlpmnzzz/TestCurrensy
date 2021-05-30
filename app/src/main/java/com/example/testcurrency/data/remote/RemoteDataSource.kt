package com.example.testcurrency.data.remote

import com.example.testcurrency.data.model.remote.CurrencyResponseEntity
import com.example.testcurrency.data.remote.api.CurrencyService
import com.example.testcurrency.data.util.NetworkUtils
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCurrency(name: String): Response<CurrencyResponseEntity>
}

class RemoteDataSourceImp(
    private val api: CurrencyService,
    private val networkUtils: NetworkUtils
) : RemoteDataSource {
    override suspend fun getCurrency(name: String): Response<CurrencyResponseEntity> {
        if (!networkUtils.hasNetworkConnection()) {
            error("There is no internet connection")
        }
        return api.getCurrency(name)
    }
}