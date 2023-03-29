package com.example.testcurrency.usecases

import com.example.testcurrency.data.CurrencyRepository
import com.example.testcurrency.data.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConvertCurrencyUseCase(
    private val repo: CurrencyRepository
) : FlowUseCase<ConvertCurrencyParam, Float>() {
    override fun execute(parameters: ConvertCurrencyParam): Flow<Float> = getFlow(parameters)

    private fun getFlow(parameters: ConvertCurrencyParam) =
        repo.getCurrency(parameters.from).map { currency ->
            getRates(currency, parameters.to)
        }.map { rate ->
            calculateResult(rate, parameters)
        }

    private fun getRates(currency: Currency, rateName: String) =
        currency.rates[rateName]

    private fun calculateResult(
        rate: Float?,
        parameters: ConvertCurrencyParam
    ): Float {
        return rate?.let {
            parameters.amount * it
        } ?: error(("There is no currency for ${parameters.to}"))
    }

}

class ConvertCurrencyParam(val amount: Float, val from: String, val to: String)