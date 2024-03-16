package com.example.secondcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class travel_list : AppCompatActivity(), travel_adapter.OnItemClickListener {
    private val itemList = ArrayList<travel_items>()
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


    }

    override fun onItemClick(position: Int) {
        val selectedDestination = itemList[position].name
        Log.d("position","$selectedDestination")
    }
}