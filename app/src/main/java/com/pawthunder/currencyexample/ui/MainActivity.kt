package com.pawthunder.currencyexample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.pawthunder.currencyexample.R
import com.pawthunder.currencyexample.ui.rates.RatesRecyclerView
import com.pawthunder.currencyexample.util.AppExecutors
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as MaterialToolbar)
        initializeRecycler()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun initializeRecycler() {
        rates_items.layoutManager = LinearLayoutManager(this)
        rates_items.adapter = RatesRecyclerView(appExecutors)
        //TODO: add items to recyclerview
    }
}
