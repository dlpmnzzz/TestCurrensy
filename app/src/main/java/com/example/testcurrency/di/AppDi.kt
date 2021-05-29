package com.example.testcurrency.di

import com.example.testcurrency.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(listLabels) }
}

private val listLabels = listOf("RUB", "USD", "EUR")