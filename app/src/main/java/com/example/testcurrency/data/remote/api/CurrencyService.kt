package com.example.testcurrency.data.remote.api

import com.example.testcurrency.data.model.remote.CurrencyResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyService {
    @GET("latest/{currencyName}")
    suspend fun getCurrency(@Path("currencyName") currencyName: String): Response<CurrencyResponseEntity>
}