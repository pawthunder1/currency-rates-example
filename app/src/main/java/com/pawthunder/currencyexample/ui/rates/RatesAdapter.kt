package com.pawthunder.currencyexample.ui.rates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.databinding.ItemRateBinding
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.ui.common.DataBoundHolder
import com.pawthunder.currencyexample.ui.common.DataBoundListAdapter
import com.pawthunder.currencyexample.util.AppExecutors

/**
 * Adapter for showing rates.
 * @property rateItemListener Listener for sending events outside of adapter.
 */
class RatesAdapter(
    private val rateItemListener: RateItemListener,
    appExecutors: AppExecutors
) : DataBoundListAdapter<Currency, ItemRateBinding>(
    appExecutors,
    object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.shortName == newItem.shortName

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem == newItem && oldItem.outValue == newItem.outValue
    }
), View.OnFocusChangeListener {

    override fun createBinding(parent: ViewGroup): ItemRateBinding =
        DataBindingUtil.inflate<ItemRateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rate,
            parent,
            false
        ).apply {
            rateValue.onFocusChangeListener = this@RatesAdapter
        }

    override fun bind(binding: ItemRateBinding, item: Currency, position: Int) {
        binding.item = item
    }

    override fun onViewDetachedFromWindow(holder: DataBoundHolder<ItemRateBinding>) {
        super.onViewDetachedFromWindow(holder)
        val view = holder.binding.rateValue
        if (view.hasFocus()) {
            // The input had focus but is no longer in window so keyboard should be hidden if wasn't
            val inputManager =
                (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            rateItemListener.continueRequests()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        val currency = view?.tag
        if (hasFocus && currency is Currency && view is EditText) {
            // Rate input was focused and should be on top
            rateItemListener.onInputFocused(view, currency)
            view.setSelection(view.text.length)
        }
    }
}
