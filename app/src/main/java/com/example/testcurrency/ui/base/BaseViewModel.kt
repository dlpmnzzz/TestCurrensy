package com.example.testcurrency.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.testcurrency.utils.SingleLiveEvent
import java.lang.Exception

abstract class BaseViewModel :ViewModel() {

    private val _message = SingleLiveEvent<String>()

    val message : LiveData<String> = _message

    fun showErrorMessage(error: Exception) {
        _message.value = error.message
    }
}