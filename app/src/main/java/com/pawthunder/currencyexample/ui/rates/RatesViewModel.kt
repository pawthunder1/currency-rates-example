package com.pawthunder.currencyexample.ui.rates

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.repository.RatesRepository
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val app: Application
) : ViewModel() {

    val rates = MutableLiveData<List<Currency>>()

    fun requestRates(currencyShort: CurrencyShortName) {
        ratesRepository.loadRates(currencyShort, rates, app)
    }

}
