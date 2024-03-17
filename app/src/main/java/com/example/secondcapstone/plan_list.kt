package com.example.secondcapstone

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class plan_list : AppCompatActivity(), plan_adpater.OnItemClickListener {
    private val itemList = ArrayList<plan_items>() //data class로 리스트 선언)
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)
        Log.d("position", "hi")
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            Log.d("position", "resultLauncher is running")
            if (result.resultCode == RESULT_OK){
                val addedList = result.data?.getStringArrayListExtra("addedList")
                val a = result.data?.getStringExtra("a")
                Log.d("position", "here is plan_list and list is $addedList")

                Log.d("position", "a is $a")
            }
        }

        val addedList = intent.getStringArrayListExtra("addedList")
        Log.d("position", "here is plan_list. $addedList")


        val dateList = intent.getStringArrayListExtra("dateList") //Calendar -> autoGenerate -> 여기 순으로 데이터 받음
        Log.d("ThirdList", "$dateList")

        val next = findViewById<Button>(R.id.next)
        // 선택 완료 버튼 이벤트 처리. 추후 다음 액티비티로 이동하게 변경
        next.setOnClickListener {
            Toast.makeText(this, "next",Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)


        dateList?.withIndex()?.forEach { (index, date) -> //dateList가 null이 아닌 경우에만 withIndex 함수 호출
            val day = (index + 1).toString()
            val planItem = plan_items(day, date)
            itemList.add(planItem)
        }

        Log.d("postion_planItem", "$itemList")

        val rc_adapter = plan_adpater(itemList, this) //어댑터 생성. 데이터 연결
        //rc_adapter.notifyDataSetChanged()
        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // parent layout 밑에 새로운 LinearLayout 생성
        val parentLayout = findViewById<LinearLayout>(R.id.parent)

        val newLinearLayout = LinearLayout(this)
        newLinearLayout.orientation = LinearLayout.VERTICAL
        newLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // 새로운 TextView 생성
        //addedList가 null이 아니고, 비어있지 않을 경우 실행
        if (addedList != null && addedList.isNotEmpty()) {
            // addedList의 값들로 TextView를 생성하고 LinearLayout에 추가
            for (item in addedList) {
                val newTextView = TextView(this)
                newTextView.text = item // TextView의 text 속성을 리스트의 값으로 설정

                // 왼쪽 margin을 10으로 설정
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = 20
                layoutParams.weight = 1F

                newTextView.layoutParams = layoutParams
                newTextView.setTextColor(android.graphics.Color.parseColor("#000000"))
                val font = ResourcesCompat.getFont(this, R.font.jua_ttf)
                newTextView.setTypeface(font, Typeface.BOLD)

                // 삭제 버튼 생성
                val deleteButton = TextView(this)
                deleteButton.text = "x"
                val deleteButtonLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                deleteButtonLayoutParams.rightMargin = 30
                deleteButton.layoutParams = deleteButtonLayoutParams
                deleteButton.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"))
                deleteButton.gravity = Gravity.END // 텍스트 오른쪽 정렬

                // 삭제 버튼 클릭 이벤트 처리
                deleteButton.setOnClickListener {
                    parentLayout.removeView(newLinearLayout)
                    //taglist.remove(tag_item) 일단 보류
                }

                // 생성한 TextView와 삭제 버튼을 LinearLayout에 추가
                newLinearLayout.addView(newTextView)
                newLinearLayout.addView(deleteButton)
            }
        }
        // LinearLayout을 부모 레이아웃에 추가
        parentLayout.addView(newLinearLayout)


    }
    override fun onItemClick(position: Int) {
//        val selectedDestination = itemList[position].date
//        Log.d("position","$selectedDestination in onItemClick")
        Toast.makeText(this, "next",Toast.LENGTH_SHORT).show()
        var intent = Intent(this, travel_list::class.java)
        resultLauncher.launch(intent)
    }

}