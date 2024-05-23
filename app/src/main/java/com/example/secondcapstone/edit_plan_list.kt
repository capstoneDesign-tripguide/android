package com.example.secondcapstone

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class edit_plan_list : AppCompatActivity() {
    private var finalTravelList = mutableListOf<List<informationOf_place>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plan_list)

        // 네비게이션 요소들
        val navBtn = findViewById<Button>(R.id.testbtn)
        val navCloseBtn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val editUserBtn = findViewById<Button>(R.id.btn_edituser)

        navBtn.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        navCloseBtn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        editUserBtn.setOnClickListener {
            val intent = Intent(this, edit_user::class.java)
            startActivity(intent)
        }

        // 인텐트로부터 데이터 받기
        val listSize = intent.getIntExtra("listSize", 0)
        for (i in 0 until listSize) {
            val parcelableArray = intent.getParcelableArrayExtra("placesList_$i")
            val innerList = parcelableArray?.map { it as informationOf_place } ?: listOf()
            finalTravelList.add(innerList)
        }

        // 지도 보기 버튼 설정
        val goToMapButton = findViewById<Button>(R.id.go_to_map)
        goToMapButton.setOnClickListener {
            val intent = Intent(this, map::class.java)
            for (i in finalTravelList.indices) { // MutableList<List<informationOf_place>>를 인덱스 단위로 put
                val innerList = finalTravelList[i]
                val parcelableArray = innerList.toTypedArray()
                intent.putExtra("placesList_$i", parcelableArray)
            }
            intent.putExtra("listSize", finalTravelList.size)
            startActivity(intent)
        }

        // parent 레이아웃 설정
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        // finalTravelList 순회하면서 뷰 생성
        finalTravelList.forEachIndexed { index, dailyPlaces ->
            // Day TextView 생성 및 추가
            val dayTextView = TextView(this).apply {
                text = "Day ${index + 1}"
                textSize = 30f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            linearLayout.addView(dayTextView)

            // 각 Day에 해당하는 여행지를 TextView로 생성하여 추가 및 해당 여행지에 대한 버튼 생성 및 추가
            dailyPlaces.forEach { place ->
                // 새로운 LinearLayout 생성 (여행지와 버튼을 수평으로 배치하기 위함)
                val placeLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                val placeTextView = TextView(this).apply {
                    text = place.displayName
                    textSize = 20f
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setTextColor(Color.BLACK)
                }

                placeLayout.addView(placeTextView)

                // 여행지에 대한 버튼 생성 및 추가
                val deleteButton = Button(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = (5 * resources.displayMetrics.density).toInt()
                    }
                    background = ContextCompat.getDrawable(context, R.drawable.btn_repple_ex)
                    setTypeface(ResourcesCompat.getFont(context, R.font.jua_ttf), Typeface.NORMAL)
                    textSize = 20f
                    text = "삭제"
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                deleteButton.setOnClickListener {
                    linearLayout.removeView(placeLayout)
                    finalTravelList[index] = finalTravelList[index].filter { it.displayName != place.displayName }
                    Log.d("0330", "$finalTravelList")
                }

                placeLayout.addView(deleteButton)
                linearLayout.addView(placeLayout)
            }
        }
    }
}
