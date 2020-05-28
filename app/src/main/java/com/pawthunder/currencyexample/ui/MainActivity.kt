package com.pawthunder.currencyexample.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.db.Currency
import com.pawthunder.currencyexample.ui.rates.RateClickListener
import com.pawthunder.currencyexample.ui.rates.RatesAdapter
import com.pawthunder.currencyexample.ui.rates.RatesViewModel
import com.pawthunder.currencyexample.util.AppExecutors
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector, RateClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val ratesViewModel: RatesViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as MaterialToolbar)

        initializeRecycler()

        ratesViewModel.rates.observe(this, Observer { items ->
            Timber.i("postmessage -> set new items")
            val adapter = rates_items.adapter

            /*for (item in items) {
                item.outValue.postValue(item.rating * (ratesViewModel.convertValue.value ?: 1.0))
            }*/

            if (adapter is RatesAdapter) {
                adapter.submitList(items)
            }
        })

        ratesViewModel.shouldRequest.observe(this, Observer { value ->
            if (value) {
                ratesViewModel.requestRates()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        ratesViewModel.shouldRequest.value = true
    }

    override fun onInputFocused(view: View?, item: Currency) {
        if (view is EditText) {
            ratesViewModel.changeFirstItem(item)
        }
    }

    override fun continueRequests() {
        ratesViewModel.shouldRequest.value = true
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun initializeRecycler() {
        rates_items.layoutManager = LinearLayoutManager(this)
        rates_items.adapter = RatesAdapter(/*this,*/ this, appExecutors)
    }
}
