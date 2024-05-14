package com.example.secondcapstone

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class autoGenerate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auto_generate)
        val dateList = intent.getStringArrayListExtra("dateList") //Calendar에서 받은 날짜
        Log.d("secondList", "$dateList")

        //이 리스트의 내용을 서버로 넘겨주면 된다.
        var taglist = mutableListOf<String>()  //var 리스트니까 mutableListOf로 선언

        val addBtn = findViewById<Button>(R.id.nextBtn)
        addBtn.setOnClickListener {
            Log.d("taglist", "taglist is $taglist")
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://my.url/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            val apiService = retrofit.create(retroTaglist::class.java)
//            val call = apiService.sendTagList(taglist)
//            call.enqueue(object : retrofit2.Callback<retroTaglistResponse> {
//                override fun onResponse(call: Call<retroTaglistResponse>, response: Response<retroTaglistResponse>) {
//                    if (response.isSuccessful) {
//                        // 성공적으로 서버에 데이터가 전송됐을 때 처리
//                        Log.d("communication", "Success")
//                    } else {
//                        // 서버 에러 처리
//                        Log.e("communication", "Failed with response code: ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<retroTaglistResponse>, t: Throwable) {
//                    // 통신 실패 시 처리
//                    Log.e("communication", "Error: ${t.message}")
//                }
//            })

            if (planMode.Manual == true) { // 수동 모드면 plan_list.kt로
                var intent = Intent(this, plan_list::class.java)
                intent.putStringArrayListExtra("dateList", ArrayList(dateList))
                startActivity(intent)
                finish()
            }
            else { //자동 모드면 바로 map.kt로
                var intent = Intent(this, map::class.java)
                intent.putStringArrayListExtra("dateList", ArrayList(dateList))
                startActivity(intent)
                finish()
            }

        }
        val tag = findViewById<EditText>(R.id.tag)








        val parentLayout = findViewById<LinearLayout>(R.id.tag_layout)

        // 돋보기 버튼으로 여행지 입력
        tag.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2 // 오른쪽 그림

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (tag.right - tag.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val tag_item = tag.text.toString()
                    taglist.add(tag_item) //리스트에 추가

                    // 새로운 LinearLayout 생성
                    val newLinearLayout = LinearLayout(this)
                    newLinearLayout.orientation = LinearLayout.HORIZONTAL
                    newLinearLayout.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    // 새로운 TextView 생성
                    val newTextView = TextView(this)
                    newTextView.text = tag_item

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
                        taglist.remove(tag_item) //리스트에서 삭제
                    }

                    // 생성한 TextView와 삭제 버튼을 LinearLayout에 추가
                    newLinearLayout.addView(newTextView)
                    newLinearLayout.addView(deleteButton)

                    // LinearLayout을 부모 레이아웃에 추가
                    parentLayout.addView(newLinearLayout)

                    // EditText 초기화
                    tag.text.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

        //navigation view
        val testBtn = findViewById<Button>(R.id.testbtn) //드로어 여는 버튼
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        testBtn.setOnClickListener { //드로어 레이아웃
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }
}
