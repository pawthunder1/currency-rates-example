package com.pawthunder.currencyexample.di

import com.pawthunder.currencyexample.CurrencyApp
import com.pawthunder.currencyexample.api.CurrencyService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideCurrencyService(): CurrencyService =
        Retrofit.Builder()
            .baseUrl(CurrencyApp.BASE_API_URL)
            .build()
            .create(CurrencyService::class.java)

}