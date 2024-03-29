package com.example.testcurrency.di

import com.example.testcurrency.data.CurrencyRepository
import com.example.testcurrency.data.RepositoryImpl
import com.example.testcurrency.data.local.AppDatabase
import com.example.testcurrency.data.local.LocalDataSource
import com.example.testcurrency.data.local.LocalDataSourceImp
import com.example.testcurrency.data.mappers.ApiToDbMapper
import com.example.testcurrency.data.mappers.DbToDomainMapper
import com.example.testcurrency.data.remote.RemoteDataSource
import com.example.testcurrency.data.remote.RemoteDataSourceImp
import com.example.testcurrency.data.remote.api.RetrofitBuilder
import com.example.testcurrency.data.util.NetworkUtils
import com.example.testcurrency.ui.home.HomeViewModel
import com.example.testcurrency.usecases.ConvertCurrencyUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get(), listLabels) }

    single { ConvertCurrencyUseCase(get()) }

    single<CurrencyRepository> { RepositoryImpl(get(), get(), get(), get()) }
    single<RemoteDataSource> { RemoteDataSourceImp(RetrofitBuilder.apiService, get()) }
    single<LocalDataSource> { LocalDataSourceImp(get()) }

    single { AppDatabase.getDatabase(androidApplication()) }
    single { DbToDomainMapper() }
    single { ApiToDbMapper() }
    single { NetworkUtils(androidApplication()) }
}

private val listLabels = listOf(
    "RUB",
    "USD",
    "EUR",
    "CHF",
    "GBP",
    "JPY",
    "UAH",
    "KZT",
    "BYN",
    "TRY",
    "CNY",
    "AUD",
    "CAD",
    "PLN"
)