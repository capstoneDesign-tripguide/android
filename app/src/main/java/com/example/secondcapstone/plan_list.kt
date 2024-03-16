package com.example.secondcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class plan_list : AppCompatActivity(), plan_adpater.OnItemClickListener {
    private val itemList = ArrayList<plan_items>() //data class로 리스트 선언)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)

        val dateList = intent.getStringArrayListExtra("dateList") //Calendar -> autoGenerate -> 여기 순으로 데이터 받음
        Log.d("ThirdList", "$dateList")

        val next = findViewById<Button>(R.id.next)

        next.setOnClickListener {
            var intent = Intent(this, travel_list::class.java) //추후 다음 activity로 고쳐야 함
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)


        dateList?.withIndex()?.forEach { (index, date) -> //dateList가 null이 아닌 경우에만 withIndex 함수 호출
            val day = (index + 1).toString()
            val planItem = plan_items(day, date)
            itemList.add(planItem)
        }

        Log.d("postion_planItem", "$itemList")

        val rc_adapter = plan_adpater(itemList, this) //어댑터 생성. 데이터 연결
        rc_adapter.notifyDataSetChanged()

        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    override fun onItemClick(position: Int) {
        val selectedDestination = itemList[position].date
        Log.d("position","$selectedDestination")
    }
}