package com.example.secondcapstone

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

class travel_list : AppCompatActivity(), travel_adapter.OnItemClickListener {
    private val itemList = mutableListOf<List<informationOf_place>>()
    private val addedList = mutableListOf<informationOf_place>()
    private var position = 0
    private var finalTravelList = mutableListOf<List<informationOf_place>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_list)

        val parcelableArray = intent.getParcelableArrayExtra("finalTravelList")
        val innerList = parcelableArray?.map {it as informationOf_place} ?: listOf()
        finalTravelList.add(innerList)
        Log.d("0524", "in travel_list, final = $finalTravelList")

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)

        val rc_adapter = travel_adapter(finalTravelList, this) //어댑터 생성. 데이터 연결
        rc_adapter.notifyDataSetChanged()

        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        position = intent.getIntExtra("position", 0)
        val nextBtn = findViewById<Button>(R.id.next)
        nextBtn.setOnClickListener {
            returnDataToPreviousActivity() //add
        }
    }

    override fun onItemClick(position: Int) {
        //추가 버튼 클릭 시 선택한 여행지 이름을 addedList에 추가
        val flattenedList = finalTravelList.flatten()
        Log.d("0524", "flatten in onClick is $flattenedList")
        val selectedDestination = flattenedList[position]
        Log.d("0524", "selected is $selectedDestination")

        //이미 존재하는 여행지인 경우
        if (selectedDestination in addedList.orEmpty()) {
            Toast.makeText(this, "이미 선택된 목적지입니다.", Toast.LENGTH_SHORT).show()
        } else {
            // selectedDestination이 addedList 안에 없으면
            addedList?.add(selectedDestination)
        }
        Log.d("position","here is travel_list and destination is $selectedDestination")
    }

    private fun returnDataToPreviousActivity() {
//        Log.d("0524", "add in travel_list is $addedList")
//        val stringList = addedList.map { it.toString() } as ArrayList<String>
//        Log.d("0524", "stringList is $stringList")
//        intent.putStringArrayListExtra("added", stringList)
//        intent.putExtra("position", position)
//
        val intent = Intent().apply {
            putParcelableArrayListExtra("addedList", ArrayList(addedList))
        }
        intent.putExtra("position", position)


        setResult(RESULT_OK, intent) // 이전 액티비티로 결과 전달
        finish() // 현재 액티비티 종료
    }

}