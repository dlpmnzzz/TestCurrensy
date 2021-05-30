package com.example.testcurrency.usecases

import com.example.testcurrency.data.Repository
import com.example.testcurrency.utils.Result
import com.example.testcurrency.utils.convert
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class ConvertCurrencyUseCase(private val ioDispatcher: CoroutineDispatcher, private val repo: Repository) :
    BaseUseCase<ConvertCurrencyParam, Float>(ioDispatcher) {

    override suspend fun execute(parameters: ConvertCurrencyParam): Flow<Result<Float>> {
        return repo.getCurrency(parameters.from).transform {
            val item = it.convert { result ->
                val currency = result.data
                val reit = currency.rates[parameters.to]
                if (reit == null) {
                    Result.Error(Exception("There is no currency for ${parameters.to}"))
                } else {
                    Result.Success(parameters.amount * reit)
                }
            }
            emit(item)
        }
    }


}

class ConvertCurrencyParam(val amount: Float, val from: String, val to: String)