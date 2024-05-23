package com.example.secondcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Sample : AppCompatActivity() {
    private var placesList = mutableListOf<List<informationOf_place>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        // Intent로부터 데이터 수신
        val listSize = intent.getIntExtra("listSize", 0)
        for (i in 0 until listSize) {
            val parcelableArray = intent.getParcelableArrayExtra("placesList_$i")
            val innerList = parcelableArray?.map { it as informationOf_place } ?: listOf()
            placesList.add(innerList)
        }
        Log.d("placeList", "placeList in Sample: $placesList")
    }
}