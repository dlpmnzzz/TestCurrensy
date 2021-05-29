package com.example.testcurrency.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testcurrency.ui.base.BaseViewModel

class HomeViewModel(listLabels: List<String>) : BaseViewModel() {

    private val fromItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }
    private val toItems: List<CurrencyLabel> = listLabels.map { CurrencyLabel(it) }
    private val _selectedFrom = MutableLiveData<String>()
    private val _selectedTo = MutableLiveData<String>()

    val selectedFrom : LiveData<String> = _selectedFrom
    val selectedTo : LiveData<String> = _selectedTo
    val items = listLabels.toTypedArray()

    fun itemSelected(which: Int, type: String) {
        val item = getItemsByTypes(type)[which]
        item.selected = true
        setSelected(type, item.name)
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

    fun convertCurrency(emount: Double, type: String) {
        
    }

}