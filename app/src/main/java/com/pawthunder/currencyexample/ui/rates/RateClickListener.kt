package com.pawthunder.currencyexample.ui.rates

import android.widget.EditText
import android.widget.TextView
import com.pawthunder.currencyexample.db.Currency

interface RateClickListener : TextView.OnEditorActionListener {

    /**
     * EditText is focused change currency.
     * @param view Editext with focus.
     * @param item Selected currency.
     */
    fun onInputFocused(view: EditText, item: Currency)

    /**
     * Network requests can continue requesting.
     */
    fun continueRequests()
}
