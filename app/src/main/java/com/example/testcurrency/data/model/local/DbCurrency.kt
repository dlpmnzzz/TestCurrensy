package com.example.testcurrency.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "currency")
data class DbCurrency(
    @PrimaryKey
    val name: String,
    @Expose
    val rates: Map<String, Float>,
)