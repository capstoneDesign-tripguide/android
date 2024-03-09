package com.example.secondcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class plan_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            var intent = Intent(this, travel_list::class.java) //추후 다음 activity로 고쳐야 함
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)
        val itemList = ArrayList<plan_items>() //data class로 리스트 선언)
        itemList.add(plan_items("1", "3/9"))
        itemList.add(plan_items("2", "3/10"))
        itemList.add(plan_items("3", "3/11"))
        itemList.add(plan_items("4", "3/12"))
        itemList.add(plan_items("5", "3/13"))
        itemList.add(plan_items("6", "3/14"))
        itemList.add(plan_items("7", "3/15"))
        itemList.add(plan_items("7", "3/16"))
        itemList.add(plan_items("8", "3/17"))
        itemList.add(plan_items("9", "3/18"))
        itemList.add(plan_items("10", "3/19"))


        val rc_adapter = plan_adpater(itemList) //어댑터 생성. 데이터 연결
        rc_adapter.notifyDataSetChanged()

        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}