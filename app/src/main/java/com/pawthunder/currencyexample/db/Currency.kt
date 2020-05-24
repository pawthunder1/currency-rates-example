package com.pawthunder.currencyexample.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pawthunder.currencyexample.ui.rates.CurrencyShortName

/**
 * Object containing essential data about currency.
 * @constructor Constructor of currency object saved in database.
 * @property id Unique identifier for every currency.
 * @property name Name of currency.
 * @property shortName Short name of currency.
 * @property rating Current rate of currency to base currency.
 */
@Entity
data class Currency(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String,
    var shortName: CurrencyShortName,
    var rating: Double = 1.0,
    var value: Double = 1.0
) : Parcelable {

    /**
     * Currency can be converted into parcel and send between activities
     * @constructor Constructor creating currency from [Parcel]
     * @param parcel [Parcel] containing data for creation of [Currency]
     * @see Parcelable
     */
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readSerializable() as CurrencyShortName,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeLong(id)
            writeString(name)
            writeSerializable(shortName)
            writeDouble(rating)
            writeDouble(value)
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Currency> {

        override fun createFromParcel(parcel: Parcel) = Currency(parcel)

        override fun newArray(size: Int): Array<Currency?> = arrayOfNulls(size)
    }
}
