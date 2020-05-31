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
    val convertValue = MutableLiveData(1.0)

    fun requestRates() {
        defaultCurrency.value?.let {
            ratesRepository.loadRates(it, rates, shouldRequest, app)
        }

        Handler().postDelayed({
            if (shouldRequest.value == true) {
                shouldRequest.value = true
            }
        }, 1000)
    }

    // TODO: reload items during text editing by user maybe listen to keyboard key press
    fun reloadRates() {
        if (rates.value != null)
            rates.postValue(rates.value)
    }

    fun changeFirstItem(item: Currency) {
        val list = rates.value
        if (list?.size ?: 0 > 0 && list?.get(0) != item) {
            shouldRequest.value = false
            defaultCurrency.value = item.shortName
            val items = recalculateItems(list, item)
            rates.value = items
        }
    }

    private fun recalculateItems(list: List<Currency>?, newBase: Currency): List<Currency> {
        if (list.isNullOrEmpty())
            return ArrayList()

        val result = ArrayList<Currency>()
        val newBaseRatio = 1.0 / newBase.rating

        for (item in list) {
            val currency = Currency(
                item.shortName,
                item.name,
                item.rating * newBaseRatio
            )
            if (currency.shortName == newBase.shortName) {
                result.add(0, currency)
            } else {
                result.add(currency)
            }
        }
        result[0].rating = 1.0

        return result
    }
}