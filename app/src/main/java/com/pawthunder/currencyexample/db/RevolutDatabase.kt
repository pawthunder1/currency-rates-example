package com.pawthunder.currencyexample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Main database of application
 */
@Database(
    entities = [Currency::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class RevolutDatabase : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao
}
