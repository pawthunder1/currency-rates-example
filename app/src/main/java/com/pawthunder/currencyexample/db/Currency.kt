package com.pawthunder.currencyexample.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String,
    var rating: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeLong(id)
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