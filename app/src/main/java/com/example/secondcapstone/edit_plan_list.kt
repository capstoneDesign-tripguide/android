package com.example.secondcapstone

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class edit_plan_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plan_list)

        //------------------네비게이션 요소들------------------
        val navBtn = findViewById<Button>(R.id.testbtn)
        val nav_close_btn = findViewById<Button>(R.id.close_nav_btn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navLayout =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)
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
        //------------------네비게이션 요소들------------------

        val go_to_map_button = findViewById<Button>(R.id.go_to_map)
        go_to_map_button.setOnClickListener {
            var intent = Intent(this, map::class.java)
            startActivity(intent)
        }

        val finalTravelList =
            intent.getSerializableExtra("listKey") as? ArrayList<ArrayList<String>?> //serializable을 ArrayList로 받음
        Log.d("0329", "in edit, finalTravelList is $finalTravelList")
        Log.d("0329", "in edit, index[0] is ${finalTravelList!![0]}")


        // parent 레이아웃 설정
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        // finalTravelList 순회하면서 뷰 생성
        finalTravelList?.forEachIndexed { index, dailyPlaces ->
            // Day TextView 생성 및 추가
            val dayTextView = TextView(this).apply {
                text = "Day ${index + 1}"
                textSize = 30f
                setTypeface(null, Typeface.BOLD)
            }
            dayTextView.setTextColor(Color.BLACK)
            linearLayout.addView(dayTextView)

            // 각 Day에 해당하는 여행지를 TextView로 생성하여 추가 및 해당 여행지에 대한 버튼 생성 및 추가
            dailyPlaces?.forEach { place ->
                // 새로운 LinearLayout 생성 (여행지와 버튼을 수평으로 배치하기 위함)
                val placeLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                }

                // 여행지 TextView 생성 및 추가
                val placeTextView = TextView(this).apply {
                    text = place
                    textSize = 16f
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }
                val place = placeTextView.text.toString()

                placeLayout.addView(placeTextView)

                // 여행지에 대한 버튼 생성 및 추가
                val button = Button(this).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                        bottomMargin = (5 * resources.displayMetrics.density).toInt()
                    }
                    background = ContextCompat.getDrawable(context, R.drawable.btn_repple_ex)
                    setTypeface(ResourcesCompat.getFont(context, R.font.jua_ttf), Typeface.NORMAL)
                    textSize = 20f
                    text = "삭제"
                }
                button.setOnClickListener {
                    placeLayout.removeAllViews()
                    //taglist.remove(tag_item) //리스트에서 삭제
                    finalTravelList.forEach { innerList ->
                        innerList!!.removeAll { it == place }
                    }
                    Log.d("0330","$finalTravelList")
                }
                placeLayout.addView(button)

                // 최종적으로 placeLayout을 linearLayout에 추가
                linearLayout.addView(placeLayout)
            }
        }
    }
}