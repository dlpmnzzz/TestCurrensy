package com.example.testcurrency.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val URL = "https://open.exchangerate-api.com/v6/"

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: CurrencyService = getRetrofit().create(CurrencyService::class.java)
}