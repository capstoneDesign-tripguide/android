//지금 고쳐야 할 문제
//1. travel_list에서 가져온 여행지가 VERTICAL이 아닌 HORIZONAL로 생성됨
package com.example.secondcapstone

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
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
    private var addedList: ArrayList<String>? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var newLinearLayoutIds = ArrayList<String>() // 전역 변수로 선언


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val addedList = result.data?.getStringArrayListExtra("addedList")
                val a = result.data?.getStringExtra("a")
                Log.d("position", "here is plan_list and addedList is $addedList")
                Log.d("position", "a is $a")
                for (i in 0 until newLinearLayoutIds.size) {
                    val parentLayout = resources.getIdentifier(newLinearLayoutIds[i], "id", packageName)
                    Log.d("test0321", "addedList is $addedList in launcher")
                    makeSetOnClickListener(parentLayout, i, addedList)
                }
                //travel_list에서 여행지 선택하고 왔으면 makeLinearLayout
//                makeLinearLayout(itemList, addedList)
            }
        }

        val dateList = intent.getStringArrayListExtra("dateList") //Calendar -> autoGenerate -> 여기 순으로 데이터 받음

        val next = findViewById<Button>(R.id.next)
        // 선택 완료 버튼 이벤트 처리. 추후 다음 액티비티로 이동하게 변경
        next.setOnClickListener {
            Toast.makeText(this, "next",Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)

        dateList?.withIndex()?.forEach { (index, date) -> //dateList에서 day와 date를 추출한 리스트를 생성함
            //예를 들어서 dateList = [03-13, 03-14, 03-15] 이면
            //itemList = [plan_items(day=1, date=03-13), plan_items(day=2, date=03-14), plan_items(day=3, date=03-15)]
            //이 day와 date값은 makeLinewarLayout()에서 사용됨
            val day = (index + 1).toString()
            val planItem = plan_items(day, date)
            itemList.add(planItem)
        }

        val rc_adapter = plan_adpater(itemList, this) //어댑터 생성. 데이터 연결
        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        makeLinearLayout(itemList)
        Log.d("test0321", "$newLinearLayoutIds")
    }
    override fun onItemClick(position: Int) {
        Toast.makeText(this, "next",Toast.LENGTH_SHORT).show()
        var intent = Intent(this, travel_list::class.java)
        Log.d("position", "launched")
        resultLauncher.launch(intent)
    }

    //travel_list에서 선택해온 여행지를 추가함
    private fun makeTextView(itemList: ArrayList<plan_items> , parentLayout: LinearLayout, addedList: ArrayList<String>?){
        //itemList = [plan_items(day=1, date=03-14), plan_items(day=2, date=03-15), plan_items(day=3, date=03-16)]
        Log.d("position","call makeTextView(). itemList is ${itemList}")
        Log.d("position","call makeTextView(). addedList is ${addedList}")

        val newLinearLayout = LinearLayout(this)
        newLinearLayout.orientation = LinearLayout.VERTICAL
        newLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        Log.d("position", "makeTextView outside if")
        if (addedList != null && addedList!!.isNotEmpty()) {
            Log.d("position","makeTextView in if")
            // addedList의 값들로 TextView를 생성하고 LinearLayout에 추가
            for (item in addedList!!) {
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

    //캘린더에서 설정한 일수만큼 밑에 뷰를 생성해 준다.
    private fun makeLinearLayout(itemList: ArrayList<plan_items>){
        //itemList는 이런식으로 생김
        //itemList = [plan_items(day=1, date=03-13), plan_items(day=2, date=03-14), plan_items(day=3, date=03-15)]
        Log.d("position", "call makeLinearLayout")
        val parentLayout = findViewById<LinearLayout>(R.id.parent_linear_detail)

        itemList?.forEach { item ->
            val itemNameTextView = TextView(this)
            val newLinearLayout = LinearLayout(this)
            newLinearLayout.orientation = LinearLayout.HORIZONTAL

            val id = View.generateViewId()
            newLinearLayout.id = id
            newLinearLayoutIds.add(id.toString())

            itemNameTextView.text = "Day"
            itemNameTextView.textSize = 25f //25dp
            itemNameTextView.setTextColor(resources.getColor(R.color.black))

            val dayTextView = TextView(this)
            dayTextView.text = item.day
            dayTextView.textSize = 15f //15dp
            dayTextView.setTextColor(resources.getColor(R.color.black))

            val dateTextView = TextView(this)
            dateTextView.text = item.date
            dateTextView.textSize = 25f //25dp
            dateTextView.setTextColor(resources.getColor(R.color.black))

            val planTextView = TextView(this)
            planTextView.text = "일정"
            planTextView.textSize = 25f
            planTextView.setTextColor(resources.getColor(R.color.black))

            if (newLinearLayout.parent != null) {
                (newLinearLayout.parent as ViewGroup).removeView(newLinearLayout)

            }
            newLinearLayout.addView(itemNameTextView)
            newLinearLayout.addView(dayTextView)
            newLinearLayout.addView(dateTextView)
            newLinearLayout.addView(planTextView)
            parentLayout.addView(newLinearLayout)
        }


    }
    private fun makeSetOnClickListener(parentLayout: Int, i: Int, addedList: ArrayList<String>?){
        val newLinearLayout = findViewById<LinearLayout>(parentLayout)
        newLinearLayout.setOnClickListener {
//            Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show()
            Log.d("test0321", "addedList is $addedList in makeSetOnClick")
            makeTextView(itemList, newLinearLayout, addedList)
        }
    }

}


