package com.example.testcurrency.di

import com.example.testcurrency.data.Repository
import com.example.testcurrency.data.RepositoryImpl
import com.example.testcurrency.ui.home.HomeViewModel
import com.example.testcurrency.usecases.ConvertCurrencyUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get(), listLabels) }

    single { ConvertCurrencyUseCase(get()) }

    single<Repository> { RepositoryImpl() }
}

private val listLabels = listOf("RUB", "USD", "EUR")