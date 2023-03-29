package com.example.testcurrency.usecases

import com.example.testcurrency.utils.Result
import com.example.testcurrency.utils.asResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .flowOn(coroutineDispatcher)
        .asResult()

    protected abstract fun execute(parameters: P): Flow<R>
}