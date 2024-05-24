//지금 고쳐야 할 문제
//1. travel_list에서 가져온 여행지가 VERTICAL이 아닌 HORIZONAL로 생성됨
//2. 삭제 버튼 에러(안 지워지거나, 한 번에 다 지워짐)
//3. 일정을 클릭해야 여행지가 추가됨 -> launcher에서 수행하도록 변경하자 -> 해결
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
import androidx.core.view.GravityCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import java.io.Serializable

class plan_list : AppCompatActivity(), plan_adpater.OnItemClickListener {
    private val itemList = ArrayList<plan_items>() //data class로 리스트 선언)
    private var addedList: ArrayList<String>? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var newLinearLayoutIds = ArrayList<String>() // 전역 변수로 선언
    private var finalTravelList = mutableListOf<List<informationOf_place>>()
    private var tmpList = mutableListOf<ArrayList<informationOf_place>?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)

        //-------------------------------------부터 네비게이션 요소들
        val navBtn = findViewById<Button>(R.id.testbtn)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)
        val edit_user_btn = findViewById<Button>(R.id.btn_edituser)

        navBtn.setOnClickListener { //드로어 레이아웃
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        nav_close_btn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        edit_user_btn.setOnClickListener {
            var intent = Intent(this, edit_user::class.java)
            startActivity(intent)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.google_login)
        }

        //로그인 버튼 클릭 시 액티비티 전환
        val loginBtn = findViewById<TextView>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            Log.d("test0121", "setOnClickLoginBtn")
            var intent = Intent(this, loginActivity::class.java)
            intent.putExtra("data", "1") //data라는 변수에 value를 put. 넣었으니 loginActivity.kt에서 받아야 함(get)
            startActivity(intent)
        }
        //-------------------------------------까지 네비게이션 요소들
        val dateList = intent.getStringArrayListExtra("dateList") ////Calendar -> autoGenerate -> 여기 순으로 데이터 받음
        val listSize = intent.getIntExtra("listSize", 0)
        for (i in 0 until listSize) {
            val parcelableArray = intent.getParcelableArrayExtra("placesList_$i")
            val innerList = parcelableArray?.map { it as informationOf_place } ?: listOf()
            finalTravelList.add(innerList)
        }
        Log.d("0524","$finalTravelList")



        //추후 travel_list와 통신할 때 사용하는 객체
            resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            Log.d("0524", "called resultLauncher")
            if (result.resultCode == RESULT_OK){

                val addedList: ArrayList<informationOf_place>? = result.data?.getParcelableArrayListExtra("addedList")
                tmpList.add(addedList)

                Log.d("0524", "modified added is $addedList")

                val position = result.data?.getIntExtra("position", 1000) //어떤 날짜 밑에 makeTravelList()할지 결정
                //왜냐면 parentLayout의 아이디를 position이 결정하니까

                if (position != null) {
                    Log.d("0524", "position is $position")
                    val parentLayout = resources.getIdentifier(newLinearLayoutIds[position],
                        "id", packageName)
                    makeTravelList(parentLayout, addedList)
                }
            }
        }




        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            Log.d("0524", "tmp is $tmpList")
            if (finalTravelList.size == itemList.size){ //모든 날짜의 일정을 채워야 이동 가능
                var intent = Intent(this, edit_plan_list::class.java)
                intent.putExtra("listSize", tmpList.size)  // 리스트의 개수 전달

                for (i in tmpList.indices) {
                    intent.putExtra("placesList_$i", tmpList[i]?.toTypedArray())
                }
                intent.putExtra("list_count", tmpList.size)  // 리스트의 개수 전달
                intent.putExtra("finalTravelList","$finalTravelList")
                intent.putExtra("listKey", finalTravelList as Serializable)
                startActivity(intent)
            }
            else { //일정이 없는 날짜가 있음
                Toast.makeText(this, "모든 날짜의 일정을 만들어주세요.",Toast.LENGTH_SHORT).show()
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcview)
        Log.d("0524", "dateList is $dateList")
        dateList?.withIndex()?.forEach { (index, date) -> //dateList에서 day와 date를 추출한 리스트를 생성함

            //예를 들어서 dateList = [03-13, 03-14, 03-15] 이면
            //itemList = [plan_items(day=1, date=03-13), plan_items(day=2, date=03-14), plan_items(day=3, date=03-15)]
            //이 day와 date값은 makeLinewarLayout()에서 사용됨
            val day = (index + 1).toString()
            val planItem = plan_items(day, date)
            itemList.add(planItem)
        }
        Log.d("0524", "itemList is $itemList")

        val rc_adapter = plan_adpater(itemList, this) //어댑터 생성. 데이터 연결
        recyclerView.adapter = rc_adapter //recyclerView에 어댑터 연결
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        makeLinearLayout(itemList)
        Log.d("test0321", "$newLinearLayoutIds")
    }

    //recyclerView의 버튼 클릭 이벤트. travel_list로 이동
    override fun onItemClick(position: Int) {
        var intent = Intent(this, travel_list::class.java)
        Log.d("position", "launched and final is $finalTravelList")

        val innerList = finalTravelList[position]
        val parcelableArray = innerList?.toTypedArray()
        Log.d("0524", "putted $parcelableArray")
        intent.putExtra("finalTravelList", parcelableArray)
        intent.putExtra("position", position)
        resultLauncher.launch(intent)

    }


    private fun makeTravelList(parentLayout: Int, addedList: ArrayList<informationOf_place>?){
        val newLinearLayout = findViewById<LinearLayout>(parentLayout)
        makeTextView(itemList, newLinearLayout, addedList)
    }
    private fun makeTextView(itemList: ArrayList<plan_items>, parentLayout: LinearLayout, addedList: ArrayList<informationOf_place>?) {

        val newLinearLayout = LinearLayout(this)
        newLinearLayout.orientation = LinearLayout.VERTICAL
        newLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        if (addedList != null && addedList.isNotEmpty()) {
            Log.d("position", "makeTextView in if")
            for (i in addedList.indices) {
                if (i < addedList.size) {
                    val name = addedList[i].displayName
                    Log.d("0524", "name is $name, i is $i")
                    val newTextView = TextView(this)
                    newTextView.text = name // TextView의 text 속성을 리스트의 값으로 설정

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    layoutParams.leftMargin = 20
                    layoutParams.weight = 1F
                    layoutParams.topMargin = 10

                    newTextView.layoutParams = layoutParams
                    newTextView.setTextColor(android.graphics.Color.parseColor("#000000"))
                    newTextView.textSize = 20f //20dp

                    val font = ResourcesCompat.getFont(this, R.font.jua_ttf)
                    newTextView.setTypeface(font, Typeface.BOLD)
                    newLinearLayout.addView(newTextView)
                }
            }
        }
        // LinearLayout을 부모 레이아웃에 추가
        parentLayout.orientation = LinearLayout.VERTICAL
        parentLayout.addView(newLinearLayout)
    }




    //캘린더에서 설정한 일수만큼 Day x Month-Day 뷰 생성
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

            val dateTextView = TextView(this)
            dateTextView.text = item.date
            dateTextView.textSize = 25f //25dp
            dateTextView.setTextColor(resources.getColor(R.color.black))

            if (newLinearLayout.parent != null) {
                (newLinearLayout.parent as ViewGroup).removeView(newLinearLayout)

            }

            newLinearLayout.addView(dateTextView)

            parentLayout.addView(newLinearLayout)

            val layoutParams_dateText = dateTextView.layoutParams as LinearLayout.LayoutParams
            layoutParams_dateText.leftMargin = 20
            layoutParams_dateText.bottomMargin = 20
        }
    }



}

