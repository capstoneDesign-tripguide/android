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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

class travel_list : AppCompatActivity(), travel_adapter.OnItemClickListener {
    private val itemList = ArrayList<travel_items>()
    private val addedList = ArrayList<String>()
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_list)


        val recyclerView = findViewById<RecyclerView>(R.id.rcview)
        //나중에 json 데이터 받아서 추가하도록 수정
        itemList.add(travel_items("여행지1", "별점1"))
        itemList.add(travel_items("여행지2", "별점2"))
        itemList.add(travel_items("여행지3", "별점3"))
        itemList.add(travel_items("여행지4", "별점4"))
        itemList.add(travel_items("여행지5", "별점5"))
        itemList.add(travel_items("여행지6", "별점6"))
        itemList.add(travel_items("여행지7", "별점7"))
        itemList.add(travel_items("여행지8", "별점8"))
        itemList.add(travel_items("여행지9", "별점9"))
        itemList.add(travel_items("여행지10", "별점10"))
        itemList.add(travel_items("여행지11", "별점11"))
        itemList.add(travel_items("여행지12", "별점12"))

        val rc_adapter = travel_adapter(itemList, this) //어댑터 생성. 데이터 연결
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
        val selectedDestination = itemList[position].name
        addedList.add(selectedDestination)
        Log.d("position","here is travel_list and destination is $selectedDestination")

    }
    private fun returnDataToPreviousActivity() {
        Log.d("position","list is composed of $addedList")
        intent.putStringArrayListExtra("addedList", addedList)
        intent.putExtra("a","b")
        Log.d("test0321", "position is $position in travel_list")
        intent.putExtra("position", position)
        setResult(RESULT_OK, intent) // 이전 액티비티로 결과 전달
        finish() // 현재 액티비티 종료
    }
}