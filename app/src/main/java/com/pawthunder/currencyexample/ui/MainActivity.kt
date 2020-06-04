package com.pawthunder.currencyexample.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var focusedEditText: EditText? = null

    private val scrollStateListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            ratesViewModel.shouldRequest.value = newState == RecyclerView.SCROLL_STATE_IDLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as MaterialToolbar)

        initializeRecycler()

        ratesViewModel.rates.observe(this, Observer { items ->
            val adapter = rates_items.adapter
            Timber.i("timber message -> new values were supplied")
            for (item in items) {
                val newValue = (item.rating * (ratesViewModel.convertValue.value ?: 1.0)).toBigDecimal()
                item.outValue = newValue
            }

            if (adapter is RatesAdapter) {
                adapter.submitList(items)
            }
        })

        ratesViewModel.shouldRequest.observe(this, Observer { value ->
            if (value) {
                ratesViewModel.requestRates()
            }
        })

        ratesViewModel.convertValue.observe(this, Observer {
            if (ratesViewModel.shouldRequest.value == false) {
                ratesViewModel.reloadRates()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        ratesViewModel.shouldRequest.value = true
    }

    override fun onDestroy() {
        rates_items.removeOnScrollListener(scrollStateListener)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ratesViewModel.shouldRequest.value = false
    }

    override fun onInputFocused(view: EditText, item: Currency) {
        focusedEditText = view
        ratesViewModel.changeFirstItem(item)
    }

    override fun continueRequests() {
        ratesViewModel.shouldRequest.value = true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (focusedEditText?.hasFocus() != true) {
            focusedEditText = null
            return false
        }

        ratesViewModel.shouldRequest.value = false

        val newValue = focusedEditText?.text?.toString()
        if(!newValue.isNullOrEmpty()) {
            ratesViewModel.convertValue.value = newValue.toDouble()
        }

        return super.onKeyUp(keyCode, event)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun initializeRecycler() {
        rates_items.layoutManager = LinearLayoutManager(this)
        rates_items.adapter = RatesAdapter(this, appExecutors)
        rates_items.addOnScrollListener(scrollStateListener)
    }
}
