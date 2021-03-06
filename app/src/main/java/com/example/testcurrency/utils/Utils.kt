package com.example.testcurrency.utils

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}