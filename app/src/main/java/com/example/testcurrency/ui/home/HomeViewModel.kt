package com.example.testcurrency.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testcurrency.ui.base.BaseViewModel
import com.example.testcurrency.usecases.ConvertCurrencyParam
import com.example.testcurrency.usecases.ConvertCurrencyUseCase
import com.example.testcurrency.utils.SingleLiveEvent

class HomeViewModel(
    private val convertUseCase: ConvertCurrencyUseCase,
    listLabels: List<String>
) :
    BaseViewModel() {

    private val fromItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }
    private val toItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }
    private val _fromResult = SingleLiveEvent<String>()
    private val _toResult = SingleLiveEvent<String>()
    private val _selectedFrom = MutableLiveData<String>("USD")
    private val _selectedTo = MutableLiveData<String>("EUR")

    val selectedFrom : LiveData<String> = _selectedFrom
    val selectedTo : LiveData<String> = _selectedTo
    val fromResult: LiveData<String> = _fromResult
    val toResult: LiveData<String> = _toResult
    val items = listLabels.toTypedArray()

    fun itemSelected(which: Int, type: String) {
        val item = getItemsByTypes(type)[which]
        item.selected = true
        setSelected(type, item.name)
    }

    fun convertCurrency(amount: Float, type: String) {
        convertUseCase.cancel()
        val from = getSelected(type)
        val to = getAnotherSelected(type)
        if (from != null && to != null) {
            val param = ConvertCurrencyParam(amount, from, to)
            convertUseCase.execute(param, onComplete = { result ->
                setCurrencyResult(type, result)
            }, onError = { showErrorMessage(it) })
        } else {
            showErrorMessage(Exception("Choose converted currency"))
        }
    }

    private fun setCurrencyResult(type: String, result: Float) {
        if (type == FROM_TYPE) {
            _toResult.value = result.toString()
        } else {
            _fromResult.value = result.toString()
        }
    }

    private fun setSelected(type: String, name: String) {
        if (type == FROM_TYPE) {
            _selectedFrom.value = name
        } else {
            _selectedTo.value = name
        }
    }


    private fun getItemsByTypes(type: String): List<CurrencyLabel> =
        if (type == FROM_TYPE) {
            fromItems
        } else {
            toItems
        }

    private fun getSelected(type: String): String? =
        if (type == FROM_TYPE) {
            _selectedFrom.value
        } else {
            _selectedTo.value
        }

    private fun getAnotherSelected(type: String) : String? =
        if (type == FROM_TYPE) {
            _selectedTo.value
        } else {
            _selectedFrom.value
        }


}