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
    val defaultCurrency = MutableLiveData<CurrencyShortName>()
    val convertValue = MutableLiveData(1.0)

    fun requestRates() {
        ratesRepository.loadRates(defaultCurrency.value ?: CurrencyShortName.EUR, rates, shouldRequest, app)

        Handler().postDelayed({
            if (shouldRequest.value == true) {
                shouldRequest.value = true
            }
        }, 1000)
    }

    fun reloadRates() {
        val items = rates.value
        if (items != null) {
            val newItems = ArrayList<Currency>()
            for (item in items) {
                newItems.add(Currency(item.shortName, item.name, item.rating))
            }
            rates.value = newItems
        }
    }

    fun changeFirstItem(item: Currency) {
        val list = rates.value
        if (list?.size ?: 0 > 0 && list?.get(0) != item) {
            convertValue.value = 1.0
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
