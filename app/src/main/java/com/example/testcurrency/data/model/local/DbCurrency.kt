package com.example.testcurrency.data.model.local

import androidx.room.Entity

@Entity(tableName = "currency")
data class DbCurrency(val name: String, val rates: HashMap<String, Float>)