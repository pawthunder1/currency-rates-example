package com.pawthunder.currencyexample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pawthunder.currencyexample.ui.rates.RatesViewModel
import com.pawthunder.currencyexample.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindTimerViewModel(ratesModel: RatesViewModel): ViewModel
}
