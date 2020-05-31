package com.pawthunder.currencyexample.api

data class RatesResponse(
    var baseCurrency: String,
    var rates: Map<String, Double>
)
