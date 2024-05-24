package com.example.secondcapstone

import android.os.Parcel
import android.os.Parcelable

data class informationOf_place(
    val displayName: String,
    val rating: Double,
    val latitude: Double,
    val longitude: Double,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(displayName)
        parcel.writeDouble(rating)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun toString(): String {
        return "$displayName,$rating,$latitude,$longitude"
    }

    companion object CREATOR : Parcelable.Creator<informationOf_place> {
        override fun createFromParcel(parcel: Parcel): informationOf_place {
            return informationOf_place(parcel)
        }

        override fun newArray(size: Int): Array<informationOf_place?> {
            return arrayOfNulls(size)
        }

        fun fromString(string: String): informationOf_place {
            val parts = string.split(",")
            return informationOf_place(
                displayName = parts[0],
                rating = parts[1].toDouble(),
                latitude = parts[2].toDouble(),
                longitude = parts[3].toDouble()
            )
        }
    }
}
