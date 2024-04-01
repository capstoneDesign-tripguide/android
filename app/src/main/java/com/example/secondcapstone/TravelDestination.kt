package com.example.secondcapstone

import android.os.Parcel
import android.os.Parcelable

class TravelDestination(val destination: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(destination)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelDestination> {
        override fun createFromParcel(parcel: Parcel): TravelDestination {
            return TravelDestination(parcel)
        }

        override fun newArray(size: Int): Array<TravelDestination?> {
            return arrayOfNulls(size)
        }
    }
}
