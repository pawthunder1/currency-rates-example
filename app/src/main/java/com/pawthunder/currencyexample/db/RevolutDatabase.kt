package com.pawthunder.currencyexample.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main database of application
 */
@Database(
    entities = [Currency::class],
    version = 1,
    exportSchema = false
)
abstract class RevolutDatabase : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao
}
