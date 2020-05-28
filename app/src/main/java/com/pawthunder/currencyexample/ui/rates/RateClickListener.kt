package com.pawthunder.currencyexample.ui.rates

import android.view.View
import com.pawthunder.currencyexample.db.Currency

interface RateClickListener {

    fun onInputFocused(view: View?, item: Currency)

    fun continueRequests()
}
