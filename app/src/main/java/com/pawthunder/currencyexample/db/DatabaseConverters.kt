package com.pawthunder.currencyexample.db

import androidx.room.TypeConverter
import com.pawthunder.currencyexample.ui.rates.CurrencyShortName

object DatabaseConverters {

    @TypeConverter
    @JvmStatic
    fun stringToCurrencyShortName(name: String) = CurrencyShortName.find(name)

    @TypeConverter
    @JvmStatic
    fun currencyShortNameToString(currency: CurrencyShortName) = currency.key
}
