package com.pawthunder.currencyexample.db

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.pawthunder.currencyexample.ui.rates.CurrencyShortName
import java.math.BigDecimal

/**
 * Object containing essential data about currency.
 * @constructor Constructor of currency object saved in database.
 * @property shortName Short name of currency.
 * @property name Name of currency.
 * @property rating Current rate of currency to base currency.
 * @property value current value
 */
@Entity
data class Currency(
    @PrimaryKey var shortName: CurrencyShortName,
    var name: String,
    var rating: Double = 1.0
) : Parcelable {

    @Ignore
    var outValue = BigDecimal(1.0)

    /**
     * Currency can be converted into parcel and send between activities
     * @constructor Constructor creating currency from [Parcel]
     * @param parcel [Parcel] containing data for creation of [Currency]
     * @see Parcelable
     */
    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as CurrencyShortName,
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeSerializable(shortName)
            writeString(name)
            writeDouble(rating)
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Currency> {

        override fun createFromParcel(parcel: Parcel) = Currency(parcel)

        override fun newArray(size: Int): Array<Currency?> = arrayOfNulls(size)
    }
}
