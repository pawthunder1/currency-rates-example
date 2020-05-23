package com.pawthunder.currencyexample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CurrencyDao : DatabaseOperationDao<Currency> {

    @Query("SELECT * FROM Currency")
    fun getItems(): LiveData<List<Currency>>
}