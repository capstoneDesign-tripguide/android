package com.example.secondcapstone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


//class plan_list_second : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_plan_list_second)
//
//        val addedList = intent.getStringArrayListExtra("addedList")
//        Log.d("position", "here is plan_list_second. $addedList")
//
//        val dateList = intent.getStringArrayListExtra("dateList")
//        createButtons(dateList)
//    }
//
//    private fun createButtons(dateList: ArrayList<String>?) {
//        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)
//
//        dateList?.let {
//            for ((index, date) in it.withIndex()) {
//                val inflatedLayout = LayoutInflater.from(this).inflate(R.layout.plan_list_layout, null) as LinearLayout
//
//                val button = inflatedLayout.findViewById<Button>(R.id.add)
//                button.id=resources.getIdentifier("button$index", "id", packageName)
//
//                val day = inflatedLayout.findViewById<TextView>(R.id.day)
//                day.text = (index + 1).toString()
//
//                val date = inflatedLayout.findViewById<TextView>(R.id.date)
//                date.text = dateList[index]
//
//                //장소 편집 누르면 여행지 추가 액티비티로 이동
//                //travel_list에서 액티비티 리스트를 getExtraput받고, 이걸 button 밑에 추가하자.
//                //새 레이아웃을 띄우기엔 한계가 있고 트리플도 이런 방식을 채택했음
//                button.setOnClickListener {
//                    Toast.makeText(this,"$index", Toast.LENGTH_SHORT).show()
//                    Log.d("travel","1")
//                    var intent = Intent(this, travel_list::class.java)
//                    Log.d("travel","2")
//                    startActivity(intent)
//                }
//
//                buttonContainer.addView(inflatedLayout)
//                Log.d("pls","$index")
//            }
//        }
//    }
//    private fun onAddButtonClick(index: Int, date: String) {
//        // "장소 편집" 버튼이 클릭되었을 때의 동작
//        Toast.makeText(this,"$index", Toast.LENGTH_SHORT)
//    }
//}
class plan_list_second : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list_second)

        val dateList = intent.getStringArrayListExtra("dateList")
        val addedList = intent.getStringArrayListExtra("addedList")
        Log.d("position", "here is plan_list_second and list is $addedList")
        createButtons(dateList, addedList)
    }

    private fun createButtons(dateList: ArrayList<String>?, addedList: ArrayList<String>?) {
        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContainer)

        dateList?.let {
            for ((index, date) in it.withIndex()) {
                val inflatedLayout = LayoutInflater.from(this).inflate(R.layout.plan_list_layout, null) as LinearLayout

                val button = inflatedLayout.findViewById<Button>(R.id.add)
                button.id=resources.getIdentifier("button$index", "id", packageName)

                val day = inflatedLayout.findViewById<TextView>(R.id.day)
                day.text = (index + 1).toString()

                val date = inflatedLayout.findViewById<TextView>(R.id.date)
                date.text = dateList[index]

                buttonContainer.addView(inflatedLayout)

                button.setOnClickListener {
                    Toast.makeText(this,"$index", Toast.LENGTH_SHORT).show()
                    Log.d("travel","1")
                    var intent = Intent(this, travel_list::class.java)
                    Log.d("travel","2")
                    startActivity(intent)
                }
            }
        }
    }
}