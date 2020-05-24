package com.pawthunder.currencyexample.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.ui.CurrencyShortName
import timber.log.Timber
import java.lang.IllegalArgumentException

object ResourceConverters {

    /**
     * Find currency flag image based on it's [CurrencyShortName].
     * @param currency Currency for picking correct flag.
     * @return Drawable resource with flag.
     */
    @DrawableRes
    fun findCurrencyImageRes(currency: CurrencyShortName?): Int {
        return when (currency) {
            CurrencyShortName.AUD -> R.drawable.australia_flag_round
            CurrencyShortName.BGN -> R.drawable.bulgaria_flag_round
            CurrencyShortName.BRL -> R.drawable.brazil_flag_round
            CurrencyShortName.CAD -> R.drawable.canada_flag_round
            CurrencyShortName.CHF -> R.drawable.switzerland_flag_round
            CurrencyShortName.CNY -> R.drawable.china_flag_round
            CurrencyShortName.CZK -> R.drawable.czech_flag_round
            CurrencyShortName.DKK -> R.drawable.denmark_flag_round
            CurrencyShortName.EUR -> R.drawable.europe_flag_round
            CurrencyShortName.GBP -> R.drawable.united_kingdom_flag_round
            CurrencyShortName.HKD -> R.drawable.china_flag_round
            CurrencyShortName.HRK -> R.drawable.croatia_flag_round
            CurrencyShortName.HUF -> R.drawable.hungary_flag_round
            CurrencyShortName.IDR -> R.drawable.indonesia_flag_round
            CurrencyShortName.ILS -> R.drawable.israel_flag_round
            CurrencyShortName.INR -> R.drawable.india_flag_round
            CurrencyShortName.ISK -> R.drawable.iceland_flag_round
            CurrencyShortName.JPY -> R.drawable.japan_flag_round
            CurrencyShortName.KRW -> R.drawable.south_korea_flag_round
            CurrencyShortName.MXN -> R.drawable.mexico_flag_round
            CurrencyShortName.MYR -> R.drawable.malaysia_flag_round
            CurrencyShortName.NOK -> R.drawable.norway_flag_round
            CurrencyShortName.NZD -> R.drawable.new_zealand_flag_round
            CurrencyShortName.PHP -> R.drawable.philippines_flag_round
            CurrencyShortName.PLN -> R.drawable.poland_flag_round
            CurrencyShortName.RON -> R.drawable.romania_flag_round
            CurrencyShortName.RUB -> R.drawable.russia_flag_round
            CurrencyShortName.SEK -> R.drawable.sweden_flag_round
            CurrencyShortName.SGD -> R.drawable.singapore_flag_round
            CurrencyShortName.THB -> R.drawable.thailand_flag_round
            CurrencyShortName.USD -> R.drawable.usa_flag_round
            CurrencyShortName.ZAR -> R.drawable.south_africa_flag_round
            else -> {
                // Ideally this send event to firebase about missing flag
                Timber.e(IllegalArgumentException("Missing flag for currency ${currency?.key}"))
                R.drawable.ic_no_flag
            }
        }
    }

    /**
     * Find currency name based on it's [CurrencyShortName].
     * @param currency Currency for picking correct name.
     * @return String resource with name.
     */
    @StringRes
    fun findCurrencyNameRes(currency: CurrencyShortName?): Int {
        return when (currency) {
            CurrencyShortName.AUD -> R.string.australian_dollar
            CurrencyShortName.BGN -> R.string.bulgarian_lev
            CurrencyShortName.BRL -> R.string.brazilian_real
            CurrencyShortName.CAD -> R.string.canadian_dollar
            CurrencyShortName.CHF -> R.string.swiss_franc
            CurrencyShortName.CNY -> R.string.chinese_yuan
            CurrencyShortName.CZK -> R.string.czech_koruna
            CurrencyShortName.DKK -> R.string.danish_krone
            CurrencyShortName.EUR -> R.string.euro
            CurrencyShortName.GBP -> R.string.pound_sterling
            CurrencyShortName.HKD -> R.string.hong_kong_dollar
            CurrencyShortName.HRK -> R.string.croatian_kuna
            CurrencyShortName.HUF -> R.string.hungarian_forint
            CurrencyShortName.IDR -> R.string.indonesian_rupiah
            CurrencyShortName.ILS -> R.string.israeli_shekel
            CurrencyShortName.INR -> R.string.indonesian_rupiah
            CurrencyShortName.ISK -> R.string.icelandic_krona
            CurrencyShortName.JPY -> R.string.japanese_yen
            CurrencyShortName.KRW -> R.string.south_korean_won
            CurrencyShortName.MXN -> R.string.mexican_peso
            CurrencyShortName.MYR -> R.string.malaysian_ringgit
            CurrencyShortName.NOK -> R.string.norwegian_krone
            CurrencyShortName.NZD -> R.string.new_zealand_dollar
            CurrencyShortName.PHP -> R.string.philippine_peso
            CurrencyShortName.PLN -> R.string.polish_zloty
            CurrencyShortName.RON -> R.string.romanian_leu
            CurrencyShortName.RUB -> R.string.russian_rouble
            CurrencyShortName.SEK -> R.string.swedish_krona
            CurrencyShortName.SGD -> R.string.singapore_dollar
            CurrencyShortName.THB -> R.string.thai_baht
            CurrencyShortName.USD -> R.string.us_dollar
            CurrencyShortName.ZAR -> R.string.south_african_rand
            else -> {
                // Ideally this send event to firebase about missing name
                Timber.e(IllegalArgumentException("Missing name for currency ${currency?.key}"))
                -1
            }
        }
    }
}
