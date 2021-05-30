package com.example.testcurrency.data.model.remote

import com.google.gson.annotations.SerializedName

class CurrencyResponseEntity(
    @SerializedName("base_code")
    val name: String,
    val rates: Map<String, Float>
)