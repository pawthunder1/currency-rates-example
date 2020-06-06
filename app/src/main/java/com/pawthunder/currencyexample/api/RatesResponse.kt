package com.pawthunder.currencyexample.api

/**
 * Parsed response containing rates and base currency received from network.
 * @constructor Constructor matches property names and structure of response received from network.
 * @property baseCurrency Name of currency set as base to calculate ratings of other currencies.
 * @property rates Map of received currencies with currency name as key and rating as value.
 */
data class RatesResponse(
    var baseCurrency: String,
    var rates: Map<String, Double>
)
