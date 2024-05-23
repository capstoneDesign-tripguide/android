package com.example.secondcapstone

import android.os.Parcel
import android.os.Parcelable

data class informationOf_place(
    val displayName: String,
    val rating: Double,
    val latitude: Double,
    val longitude: Double,
) : Parcelable { //Parcelable 인터페이스는 이 데이터 클래스를 직렬화해서 intent로 보낼 수 있게 함
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

    companion object CREATOR : Parcelable.Creator<informationOf_place> {
        override fun createFromParcel(parcel: Parcel): informationOf_place {
            return informationOf_place(parcel)
        }

        override fun newArray(size: Int): Array<informationOf_place?> {
            return arrayOfNulls(size)
        }
    }
}
