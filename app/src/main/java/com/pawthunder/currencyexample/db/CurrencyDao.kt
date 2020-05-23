package com.pawthunder.currencyexample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

/**
 * Dao for interaction with currency table
 */
@Dao
interface CurrencyDao : DatabaseOperationDao<Currency> {

    @Query("SELECT * FROM Currency")
    fun getItems(): LiveData<List<Currency>>
}
