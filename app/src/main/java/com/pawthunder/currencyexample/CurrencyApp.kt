package com.pawthunder.currencyexample

import android.app.Application
import com.pawthunder.currencyexample.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class CurrencyApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    companion object {
        internal const val BASE_API_URL = "https://hiring.revolut.codes/api/android/"
    }
}