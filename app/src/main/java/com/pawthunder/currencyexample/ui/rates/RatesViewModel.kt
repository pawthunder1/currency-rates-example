package com.pawthunder.currencyexample.ui.rates

import android.app.Application
import android.os.Handler
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
    val shouldRequest = MutableLiveData<Boolean>()
    val defaultCurrency = MutableLiveData<CurrencyShortName>()
    val convertValue = MutableLiveData(1.0)

    /**
     * From this moment items are being requested from repository every second as long as
     * [shouldRequest] value is true
     */
    fun requestRates() {
        ratesRepository.loadRates(
            defaultCurrency.value ?: CurrencyShortName.EUR,
            rates,
            shouldRequest,
            app
        )

        Handler().postDelayed({
            if (shouldRequest.value == true) {
                shouldRequest.value = true
            }
        }, 1000)
    }

    /**
     * All rates should be reloaded and showed new changes. New list must be created
     * to show changes in UI.
     */
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

    /**
     * First item was changed and all items must be recalculated with new rating to reflect new
     * base value.
     * @param firstItem New item set as first.
     */
    fun changeFirstItem(firstItem: Currency) {
        val list = rates.value
        // Check if first item was actually changed
        if (list?.size ?: 0 > 0 && list?.get(0)?.shortName != firstItem.shortName) {
            convertValue.value = 1.0
            shouldRequest.value = false
            defaultCurrency.value = firstItem.shortName
            val items = recalculateItems(list, firstItem)
            rates.value = items
        }
    }

    private fun recalculateItems(list: List<Currency>?, newBase: Currency): List<Currency> {
        if (list.isNullOrEmpty())
            return ArrayList()

        // Base ratio is calculated as rating of previous first item divided by rating of new item.
        // Since previous first item is always 1.0 we can use it as constant.
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
