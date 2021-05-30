package com.example.testcurrency.data.mappers

import com.example.testcurrency.data.model.Currency
import com.example.testcurrency.data.model.local.DbCurrency

class DbToUiMapper : Mapper<DbCurrency, Currency> {
    override fun map(item: DbCurrency): Currency {
        return Currency(item.name, item.rates)
    }
}