package com.example.secondcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class calendar_nextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_next)
        val txtview = findViewById<TextView>(R.id.exTextview)
        val startDay = intent.getStringExtra("startDay") //mainActivity.kt에서 putExtra로 넣은 데이터를 받음
        val endDay = intent.getStringExtra("endDay")

        txtview.text="출발일:${startDay}, 도착일:${endDay}"

    }
}