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


    //모든 여행지가 담겨있는 리스트. final_traveList[0]은 1일차에 가는 여행지 목록
    private var finalTravelList = ArrayList<ArrayList<String>?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_list)

        //네비게이션 요소들
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

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                //intent가 아니라 result.data?에서 값을 읽어와야 함
                val addedList = result.data?.getStringArrayListExtra("addedList")
                val position = result.data?.getIntExtra("position", 1000)
                Log.d("test0321", "position is $position in plan_list")
                val a = result.data?.getStringExtra("a")
                Log.d("test0321", "here is plan_list and addedList is $addedList")
                Log.d("test0321", "a is $a")

                //일정마다 클릭 이벤트 추가
//                for (i in 0 until newLinearLayoutIds.size) {
//
//                    val parentLayout = resources.getIdentifier(newLinearLayoutIds[i], "id", packageName)
//                    makeTravelList(parentLayout, i, addedList)
//                }
                if (position != null) {
                    val parentLayout = resources.getIdentifier(newLinearLayoutIds[position],
                        "id", packageName)
                    makeTravelList(parentLayout, addedList)
                }

            }
        }

        val dateList = intent.getStringArrayListExtra("dateList") //Calendar -> autoGenerate -> 여기 순으로 데이터 받음

        val next = findViewById<Button>(R.id.next)
        // 선택 완료 버튼 이벤트 처리. 추후 다음 액티비티로 이동하게 변경
        next.setOnClickListener {
            if (finalTravelList.size == itemList.size){ //모든 날짜의 일정을 채움
                var intent = Intent(this, edit_plan_list::class.java)
                intent.putExtra("finalTravelList","$finalTravelList")
                intent.putExtra("listKey", finalTravelList as Serializable)
                Log.d("0329","in plan, $finalTravelList")
                startActivity(intent)
            }
            else { //일정이 없는 날짜가 있음
                Toast.makeText(this, "모든 날짜의 일정을 만들어주세요.",Toast.LENGTH_SHORT).show()
            }
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

    //recyclerView의 버튼 클릭 이벤트. travel_list로 이동
    override fun onItemClick(position: Int) {
        Log.d("test0321","$position is clicked")
//        Toast.makeText(this, "next",Toast.LENGTH_SHORT).show()
        var intent = Intent(this, travel_list::class.java)
        Log.d("position", "launched")
        intent.putExtra("position", position)
        resultLauncher.launch(intent)
    }

    //makeLinearLayout()으로 생성한 레이아웃 밑에 travel_list에서 선택해온 여행지를 추가함
    //근데 vertical이 아니라 horizon으로 여행지를 추가하는 에러 발생..
    private fun makeTextView(itemList: ArrayList<plan_items> , parentLayout: LinearLayout, addedList: ArrayList<String>?){
        //itemList = [plan_items(day=1, date=03-14), plan_items(day=2, date=03-15), plan_items(day=3, date=03-16)]
        Log.d("position","call makeTextView(). itemList is ${itemList}")
        Log.d("position","call makeTextView(). addedList is ${addedList}") //travel_list에서 가져온 여행지 목록. [여행지1, 여행지3]

        //최종 리스트에 추가
        finalTravelList.add(addedList)

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

                // 삭제 버튼 생성 -> 에러 이유를 모르겠음.. 다른 레이아웃에서 처리하도록 하자.
//                val deleteButton = TextView(this)
//                deleteButton.text = "x"
//
//                val deleteButtonLayoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                deleteButtonLayoutParams.rightMargin = 30
//                deleteButton.layoutParams = deleteButtonLayoutParams
//                deleteButton.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"))
//                deleteButton.gravity = Gravity.END // 텍스트 오른쪽 정렬
//
//                // 삭제 버튼 클릭 이벤트 처리
//                deleteButton.setOnClickListener {
//                    parentLayout.removeView(newLinearLayout)
//                    //taglist.remove(tag_item) 일단 보류
//                }

                // 생성한 TextView와 삭제 버튼을 LinearLayout에 추가
                newLinearLayout.addView(newTextView)
//                newLinearLayout.addView(deleteButton)
            }
        }
        // LinearLayout을 부모 레이아웃에 추가
        parentLayout.orientation=LinearLayout.VERTICAL
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
            

            //Day 1 03/30 일정 이런식으로 표기하려 했는데
            //그 밑에 여행지 추가하면 Day, 1, 03/30, 일정이 VERTICAL로 바뀜
            //에러 해결 불가능 -> date 외 모두 주석 처리
//            itemNameTextView.text = "Day"
//            itemNameTextView.textSize = 25f //25dp
//            itemNameTextView.setTextColor(resources.getColor(R.color.black))
//
//            val dayTextView = TextView(this)
//            dayTextView.text = item.day
//            dayTextView.textSize = 15f //15dp
//            dayTextView.setTextColor(resources.getColor(R.color.black))

            val dateTextView = TextView(this)
            dateTextView.text = item.date
            dateTextView.textSize = 25f //25dp
            dateTextView.setTextColor(resources.getColor(R.color.black))

//            val planTextView = TextView(this)
//            planTextView.text = "일정"
//            planTextView.textSize = 20f
//
//            planTextView.setTextColor(resources.getColor(R.color.black))

            if (newLinearLayout.parent != null) {
                (newLinearLayout.parent as ViewGroup).removeView(newLinearLayout)

            }
//            newLinearLayout.addView(itemNameTextView)
//            newLinearLayout.addView(dayTextView)
            newLinearLayout.addView(dateTextView)
//            newLinearLayout.addView(planTextView)
            parentLayout.addView(newLinearLayout)

            //레이아웃 조정은 부모 레이아웃에 추가 후에 설정해야 함
//            val layoutParams_planText = planTextView.layoutParams as LinearLayout.LayoutParams
//            layoutParams_planText.leftMargin = 20

            val layoutParams_dateText = dateTextView.layoutParams as LinearLayout.LayoutParams
            layoutParams_dateText.leftMargin = 20
            layoutParams_dateText.bottomMargin = 20
        }


    }

    private fun makeTravelList(parentLayout: Int, addedList: ArrayList<String>?){
        val newLinearLayout = findViewById<LinearLayout>(parentLayout)
//            Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show()
//            Log.d("test0321", "addedList is $addedList in makeSetOnClick")
        makeTextView(itemList, newLinearLayout, addedList)
    }

}

