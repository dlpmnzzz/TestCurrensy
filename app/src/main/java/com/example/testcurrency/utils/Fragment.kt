package com.example.testcurrency.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun Fragment.fragmentRepeatOnCreated(
    block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

fun <T> CoroutineScope.launchAndCollect(flow: Flow<T>, flowCollector: FlowCollector<T>) {
    launch {
        flow.collect(flowCollector)
    }
}