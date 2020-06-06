package com.pawthunder.currencyexample.ui.rates

import android.widget.EditText
import com.pawthunder.currencyexample.db.Currency

/**
 * Interface to listen for any action on rate adapter item which should be handled
 * outside of adapter.
 */
interface RateItemListener {

    /**
     * EditText is focused change currency.
     * @param view EditText with focus.
     * @param item Selected currency.
     */
    fun onInputFocused(view: EditText, item: Currency)

    /**
     * Network requests can continue requesting.
     */
    fun continueRequests()
}
