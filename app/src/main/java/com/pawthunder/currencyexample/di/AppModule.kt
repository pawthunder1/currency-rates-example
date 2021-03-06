package com.pawthunder.currencyexample.di

import android.app.Application
import androidx.room.Room
import com.pawthunder.currencyexample.RevolutApp
import com.pawthunder.currencyexample.api.RevolutService
import com.pawthunder.currencyexample.db.CurrencyDao
import com.pawthunder.currencyexample.db.RevolutDatabase
import com.pawthunder.currencyexample.util.NetworkConnectionProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideRevolutService(): RevolutService =
        Retrofit.Builder()
            .baseUrl(RevolutApp.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RevolutService::class.java)

    @Singleton
    @Provides
    fun provideRevolutDatabase(app: Application): RevolutDatabase =
        Room.databaseBuilder(app, RevolutDatabase::class.java, RevolutApp.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCurrencyDao(database: RevolutDatabase): CurrencyDao =
        database.getCurrencyDao()

    @Singleton
    @Provides
    fun provideNetworkProvider(app: Application) =
        NetworkConnectionProvider(app)

}
