package com.pawthunder.currencyexample.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pawthunder.currencyexample.ui.rates.CurrencyShortName
import com.pawthunder.currencyexample.util.ResourceConverters
import java.math.BigDecimal
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("flagCurrency")
    fun assignFlagFromName(imageView: ImageView, currency: CurrencyShortName?) {
        imageView.setImageResource(ResourceConverters.findCurrencyImageRes(currency))
    }

    @JvmStatic
    @BindingAdapter("showNumber")
    fun showRoundedDouble(textView: TextView, value: BigDecimal?) {
        textView.text = String.format(Locale.US, "%.2f", value)
    }
}
