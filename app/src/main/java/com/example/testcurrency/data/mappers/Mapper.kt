package com.example.testcurrency.data.mappers

interface Mapper<In, Out> {
    fun map(item: In) : Out
}