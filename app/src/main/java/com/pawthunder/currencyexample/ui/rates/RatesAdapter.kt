package com.pawthunder.currencyexample.ui.rates

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.databinding.ItemRateBinding
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.ui.common.DataBoundHolder
import com.pawthunder.currencyexample.ui.common.DataBoundListAdapter
import com.pawthunder.currencyexample.util.AppExecutors

class RatesAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<Currency, ItemRateBinding>(
    appExecutors,
    object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.shortName == newItem.shortName

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem == newItem
    }
) {
    override fun createBinding(parent: ViewGroup): ItemRateBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rate,
            parent,
            false
        )

    override fun bind(binding: ItemRateBinding, item: Currency, position: Int) {
        binding.item = item
    }

    override fun onViewDetachedFromWindow(holder: DataBoundHolder<ItemRateBinding>) {
        super.onViewDetachedFromWindow(holder)
        val view = holder.binding.rateValue
        if (view.hasFocus()) {
            val inputManager =
                (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
