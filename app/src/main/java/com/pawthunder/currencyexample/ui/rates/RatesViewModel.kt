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
        Timber.i("timber out -> newBase is ${newBase.name} and ratio $newBaseRatio ")

        for (item in list) {
            item.rating *= newBaseRatio
            if (item.shortName == newBase.shortName) {
                Timber.i("timber out -> newBase is ${item.name} and ratio ${item.rating} ")
                result.add(0, item)
            } else {
                result.add(item)
            }
        }
        result[0].rating = 1.0

        return result
    }
}
