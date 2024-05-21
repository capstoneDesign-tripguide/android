package com.example.secondcapstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import org.w3c.dom.Text


class select_spot : AppCompatActivity() {
    private lateinit var container: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_spot)

        //추가 버튼 누르면 여행지 전달
        val input_destination = findViewById<AutoCompleteTextView>(R.id.destination)
        val add_btn = findViewById<TextView>(R.id.add)
        add_btn.setOnClickListener {
            var intent = Intent(this, calendar::class.java)
            var travel_spot = input_destination.text.toString()
            intent.putExtra("travel_spot", "$travel_spot") //travel_spot 변수에 value를 put. 넣었으니 calendar에서 getExtra()로 받아야 함
            startActivity(intent)
            place.selected_place = travel_spot
            finish()
        }

        //여행지 목록 자동 완성
        val destination = resources.getStringArray(R.array.place)

        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, destination)
        input_destination.setAdapter(arrayAdapter)

        val close_btn = findViewById<Button>(R.id.close_btn)
        close_btn.setOnClickListener {
            finish()
        }
        var recent_searched_list = mutableListOf<String>() // var 리스트니까 mutableListOf로 선언
        //val parentLayout = findViewById<LinearLayout>(R.id.recent_searched_layout)

        //추천 여행지를 누르면 그 여행지의 텍스트가 input_destination에 입력된다.
        //배경을 TextView가 아닌 LinearLayout에 입혀서 그 레이아웃을 참조한 Textview를 참조해야 함
        val recommend1 = findViewById<LinearLayout>(R.id.recommend1)
        val rt1 = recommend1.findViewById<TextView>(R.id.recommend1_text) //recommended text
        recommend1.setOnClickListener {
            val rt1_destination = rt1.text.toString()
            input_destination.setText(rt1_destination)
        }

        val recommend2 = findViewById<LinearLayout>(R.id.recommend2)
        val rt2 = recommend2.findViewById<TextView>(R.id.recommend2_text) //recommended text
        recommend2.setOnClickListener {
            val rt2_destination = rt2.text.toString()
            input_destination.setText(rt2_destination)
        }

        val recommend3 = findViewById<LinearLayout>(R.id.recommend3)
        val rt3 = recommend3.findViewById<TextView>(R.id.recommend3_text) //recommended text
        recommend3.setOnClickListener {
            val rt3_destination = rt3.text.toString()
            input_destination.setText(rt3_destination)
        }

        val recommend4 = findViewById<LinearLayout>(R.id.recommend4)
        val rt4 = recommend4.findViewById<TextView>(R.id.recommend4_text) //recommended text
        recommend4.setOnClickListener {
            val rt4_destination = rt4.text.toString()
            input_destination.setText(rt4_destination)
        }

        val recommend5 = findViewById<LinearLayout>(R.id.recommend5)
        val rt5 = recommend5.findViewById<TextView>(R.id.recommend5_text) //recommended text
        recommend5.setOnClickListener {
            val rt5_destination = rt5.text.toString()
            input_destination.setText(rt5_destination)
        }

        val recommend6 = findViewById<LinearLayout>(R.id.recommend6)
        val rt6 = recommend6.findViewById<TextView>(R.id.recommend6_text) //recommended text
        recommend6.setOnClickListener {
            val rt6_destination = rt6.text.toString()
            input_destination.setText(rt6_destination)
        }

        val recommend7 = findViewById<LinearLayout>(R.id.recommend7)
        val rt7 = recommend7.findViewById<TextView>(R.id.recommend7_text) //recommended text
        recommend7.setOnClickListener {
            val rt7_destination = rt7.text.toString()
            input_destination.setText(rt7_destination)
        }

        val recommend8 = findViewById<LinearLayout>(R.id.recommend8)
        val rt8 = recommend8.findViewById<TextView>(R.id.recommend8_text) //recommended text
        recommend8.setOnClickListener {
            val rt8_destination = rt8.text.toString()
            input_destination.setText(rt8_destination)
        }

        val recommend9 = findViewById<LinearLayout>(R.id.recommend9)
        val rt9 = recommend9.findViewById<TextView>(R.id.recommend9_text) //recommended text
        recommend9.setOnClickListener {
            val rt9_destination = rt9.text.toString()
            input_destination.setText(rt9_destination)
        }

        val recommend10 = findViewById<LinearLayout>(R.id.recommend10)
        val rt10 = recommend10.findViewById<TextView>(R.id.recommend10_text) //recommended text
        recommend10.setOnClickListener {
            val rt10_destination = rt10.text.toString()
            input_destination.setText(rt10_destination)
        }

        val recommend11 = findViewById<LinearLayout>(R.id.recommend11)
        val rt11 = recommend11.findViewById<TextView>(R.id.recommend11_text) //recommended text
        recommend11.setOnClickListener {
            val rt11_destination = rt11.text.toString()
            input_destination.setText(rt11_destination)
        }

        val recommend12 = findViewById<LinearLayout>(R.id.recommend12)
        val rt12 = recommend12.findViewById<TextView>(R.id.recommend12_text) //recommended text
        recommend12.setOnClickListener {
            val rt12_destination = rt12.text.toString()
            input_destination.setText(rt12_destination)
        }

    }
}























