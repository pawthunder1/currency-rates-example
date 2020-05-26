package com.pawthunder.currencyexample.ui.rates

import android.app.Application
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.repository.RatesRepository
import timber.log.Timber
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val app: Application
) : ViewModel() {

    val rates = MutableLiveData<List<Currency>>()
    val shouldRequest = MutableLiveData<Boolean>()
    val defaultCurrency = MutableLiveData(CurrencyShortName.EUR)

    fun requestRates() {
        defaultCurrency.value?.let {
            Timber.i("Rates requested")
            ratesRepository.loadRates(it, rates, app)
        }

        Handler().postDelayed({
            if (shouldRequest.value == true) {
                shouldRequest.value = true
            }
        }, 1000)
    }

}
