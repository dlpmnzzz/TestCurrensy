package com.example.testcurrency.usecases

import com.example.testcurrency.data.Repository
import com.example.testcurrency.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class ConvertCurrencyUseCase(private val ioDispatcher: CoroutineDispatcher, private val repo: Repository) :
    BaseUseCase<ConvertCurrencyParam, Float>(ioDispatcher) {

    override suspend fun execute(parameters: ConvertCurrencyParam): Flow<Result<Float>> {
        return repo.getCurrency(parameters.from).transform { result ->
            when (result) {
                is Result.Success -> {
                    val currency = result.data
                    val reit = currency.rates[parameters.to]
                    if (reit == null) {
                        emit(Result.Error(Exception("There is no currency for ${parameters.to}")))
                    } else {
                        emit(Result.Success(parameters.amount * reit))
                    }
                }
                is Result.Loading -> emit(result)
                is Result.Error -> emit(result)
            }
        }
    }


}

class ConvertCurrencyParam(val amount: Float, val from: String, val to: String)