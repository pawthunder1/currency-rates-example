package com.pawthunder.currencyexample.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pawthunder.currencyexample.api.RatesResponse
import com.pawthunder.currencyexample.api.RevolutService
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.db.CurrencyDao
import com.pawthunder.currencyexample.ui.rates.CurrencyShortName
import com.pawthunder.currencyexample.util.AppExecutors
import com.pawthunder.currencyexample.util.ResourceConverters
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository class communicating with RatesViewModel and handling data to it from network or database.
 * @property appExecutors Executors for executing database and network actions on different threads.
 * @property currencyDao Dao for accessing queries upon currency table.
 * @property revolutService Network service for requesting rates.
 */
class RatesRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val currencyDao: CurrencyDao,
    private val revolutService: RevolutService
) {

    /**
     * Create [NetworkDataResource] for loading rates.
     * @param baseCurrency new base currency.
     * @param result LiveData containing result.
     * @param shouldPost LiveData indicating if result should be posted.
     * @param context Context for working with resources.
     */
    fun loadRates(
        baseCurrency: CurrencyShortName,
        result: MutableLiveData<List<Currency>>,
        shouldPost: MutableLiveData<Boolean>,
        context: Context
    ) {
        object :
            NetworkDataResource<RatesResponse, List<Currency>>(appExecutors, result) {
            override fun shouldRequest() = true

            override fun loadFromDatabase() = currencyDao.getItems()

            override fun requestFromNetwork() = revolutService.getLatestRates(baseCurrency.key)

            override fun saveCallResult(response: RatesResponse): List<Currency> {
                val rates = response.rates
                val resultItems = ArrayList<Currency>()

                val firstCurrency = createCurrency(response.baseCurrency, context)
                if (firstCurrency != null) {
                    resultItems.add(firstCurrency)
                }

                for (key in rates.keys) {
                    val currency = createCurrency(key, context, rates[key])
                    if (currency != null) {
                        resultItems.add(currency)
                    }
                }

                currencyDao.insertItems(resultItems)
                return resultItems
            }

            override fun onFailedRequest(throwable: Throwable) {
                // do nothing
            }

            override fun postValue(value: List<Currency>) {
                val newValues = value.toMutableList()
                val output = ArrayList<Currency>()

                // The part secures that all current items keep their positions and don't jump
                for (originalRate in result.value ?: ArrayList()) {
                    var removeValue: Currency? = null
                    for (newRate in newValues) {
                        if (newRate.shortName == originalRate.shortName) {
                            output.add(newRate)
                            removeValue = newRate
                            break
                        }
                    }

                    removeValue?.let {
                        newValues.remove(it)
                    }
                }

                output.addAll(newValues)

                if (shouldPost.value != false)
                    result.postValue(output)
            }
        }.loadData()
    }

    private fun createCurrency(name: String, context: Context, rate: Double? = 1.0): Currency? {
        val shortName = CurrencyShortName.find(name)

        if (shortName == CurrencyShortName.UNKNOWN) {
            // This should send event to firebase
            Timber.e(IllegalArgumentException("Currency was not found"))
            return null
        }

        return Currency(
            shortName,
            context.getString(ResourceConverters.findCurrencyNameRes(shortName)),
            rate ?: 1.0
        )
    }
}
