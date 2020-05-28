package com.pawthunder.currencyexample.ui.rates

import android.widget.EditText
import com.pawthunder.currencyexample.db.Currency

interface RateClickListener {

    fun onInputFocused(view: EditText, item: Currency)

    fun continueRequests()
}
