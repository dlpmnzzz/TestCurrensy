package com.example.testcurrency.data.mappers

import com.example.testcurrency.data.model.local.DbCurrency
import com.example.testcurrency.data.model.remote.CurrencyResponseEntity

class ApiToDbMapper : Mapper<CurrencyResponseEntity, DbCurrency> {
    override fun map(item: CurrencyResponseEntity): DbCurrency {
        return DbCurrency(item.name, item.rates)
    }
}