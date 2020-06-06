package com.pawthunder.currencyexample.ui.rates

/**
 * Enum containing all recognized currencies in application.
 * @property key Short unique name of currency.
 */
enum class CurrencyShortName(val key: String) {
    AUD("AUD"),
    BGN("BGN"),
    BRL("BRL"),
    CAD("CAD"),
    CHF("CHF"),
    CNY("CNY"),
    CZK("CZK"),
    DKK("DKK"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    HRK("HRK"),
    HUF("HUF"),
    IDR("IDR"),
    ILS("ILS"),
    INR("INR"),
    ISK("ISK"),
    JPY("JPY"),
    KRW("KRW"),
    MXN("MXN"),
    MYR("MYR"),
    NOK("NOK"),
    NZD("NZD"),
    PHP("PHP"),
    PLN("PLN"),
    RON("RON"),
    RUB("RUB"),
    SEK("SEK"),
    SGD("SGD"),
    THB("THB"),
    USD("USD"),
    ZAR("ZAR"),
    UNKNOWN("");

    companion object {
        private val map = values().associateBy(CurrencyShortName::key)

        fun find(key: String?) = map[key] ?: UNKNOWN
    }
}
