package com.pawthunder.currencyexample.di

import androidx.lifecycle.ViewModelProvider
import com.pawthunder.currencyexample.util.ViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}