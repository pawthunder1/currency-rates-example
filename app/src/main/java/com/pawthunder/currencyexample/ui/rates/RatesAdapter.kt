package com.pawthunder.currencyexample.ui.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.databinding.ItemRateBinding
import com.pawthunder.currencyexample.db.Currency
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
}
