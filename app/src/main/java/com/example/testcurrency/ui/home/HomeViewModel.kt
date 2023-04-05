package com.example.testcurrency.ui.home

import androidx.lifecycle.viewModelScope
import com.example.testcurrency.ui.base.BaseViewModel
import com.example.testcurrency.usecases.ConvertCurrencyParam
import com.example.testcurrency.usecases.ConvertCurrencyUseCase
import com.example.testcurrency.utils.Result
import com.example.testcurrency.utils.cancelIfActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val convertUseCase: ConvertCurrencyUseCase,
    listLabels: List<String>
) : BaseViewModel() {

    private var resultJob: Job? = null
    private var notConvert = false

    private val fromItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }
    private val toItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }

    private val _selectedFromCurrency = MutableStateFlow("USD")
    val selectedFromCurrency : StateFlow<String> = _selectedFromCurrency

    private val _selectedToCurrency = MutableStateFlow("EUR")
    val selectedToCurrency : StateFlow<String> = _selectedToCurrency

    private val _fromAmount = MutableStateFlow(0f)
    val fromAmount: StateFlow<String> = _fromAmount.map {
        convertAmountToString(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    private val _toAmount = MutableStateFlow(0f)
    val toAmount: StateFlow<String> = _toAmount.map {
        convertAmountToString(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val items = listLabels.toTypedArray()

    fun itemSelected(which: Int, type: String) {
        val item = getItemsByTypes(type)[which]
        item.selected = true
        setSelected(type, item.name)
    }

    fun convertCurrency(amount: Float, type: String) {
        if (notConvert) {
            return
        }
        val from = getSelected(type)
        val to = getAnotherSelected(type)
        if (from.isNotEmpty() && to.isNotEmpty()) {
            val param = ConvertCurrencyParam(amount, from, to)
            resultJob?.cancelIfActive()
            resultJob = convertUseCase(param).onEach { result ->
                precessConvertResult(type, result)
            }.launchIn(viewModelScope)
        } else {
            showError(Exception("Choose converted currency"))
        }
    }

    private fun precessConvertResult(type: String, result: Result<Float>) {
        when (result) {
            is Result.Success -> setCurrencyResult(type, result.data)
            is Result.Error -> showError(result.exception)
            is Result.Loading -> {}
        }
    }

    fun swap() {
        notConvert = true
        val tmp = _selectedToCurrency.value
        _selectedToCurrency.update { _selectedFromCurrency.value }
        notConvert = false
        _selectedFromCurrency.update { tmp }
    }

    private fun setCurrencyResult(type: String, result: Float) {
        if (type == FROM_TYPE) {
            _toAmount.value = result
        } else {
            _fromAmount.value = result
        }
    }

    private fun setSelected(type: String, name: String) {
        if (type == FROM_TYPE) {
            _selectedFromCurrency.update { name }
        } else {
            _selectedToCurrency.update { name }
        }
    }


    private fun getItemsByTypes(type: String): List<CurrencyLabel> =
        if (type == FROM_TYPE) {
            fromItems
        } else {
            toItems
        }

    private fun getSelected(type: String): String =
        if (type == FROM_TYPE) {
            _selectedFromCurrency.value
        } else {
            _selectedToCurrency.value
        }

    private fun getAnotherSelected(type: String) : String =
        if (type == FROM_TYPE) {
            _selectedToCurrency.value
        } else {
            _selectedFromCurrency.value
        }

    private fun convertAmountToString(amount: Float): String {
        return if (amount != 0f) {
            amount.toString()
        } else {
            ""
        }
    }

}