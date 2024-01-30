package com.example.secondcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.secondcapstone.databinding.ActivityMainBinding
import com.example.secondcapstone.databinding.LoginActivityBinding
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK


class MainActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var calendarBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("test0121", "keyhash : ${Utility.getKeyHash(this)}") //해시키 구하기

        //여행지 목록 자동 완성
        val destination = resources.getStringArray(R.array.place)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.destiation)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, destination)

        autoCompleteTextView.setAdapter(arrayAdapter)



        //돋보기 버튼으로 여행지 입력
        autoCompleteTextView.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2 // Index of drawableRight in the array

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (autoCompleteTextView.right - autoCompleteTextView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    var intent = Intent(this, calendar::class.java)
                    val travel_spot = autoCompleteTextView.text.toString()
                    intent.putExtra("travel_spot", travel_spot) //putExtra()는 startActivity() 이전에 실행돼야 함
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_from_top, 0) // 시작 애니메이션, 종료 애니메이션 적용. 0으로 설정하면 디폴트

                    return@setOnTouchListener true
                }
            }
            false
        }

        //로그인 버튼 클릭 시 액티비티 전환
        val loginBtn = findViewById<TextView>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            Log.d("test0121", "setOnClickLoginBtn")
            var intent = Intent(this, loginActivity::class.java)
            intent.putExtra("data", "1") //data라는 변수에 value를 put. 넣었으니 loginActivity.kt에서 받아야 함(get)
            startActivity(intent)
        }

        //수동, 자동 일정 생성 버튼 클릭 시 액티비티 전환
        val autoButton = findViewById<Button>(R.id.autoBtn)
        val directButton = findViewById<Button>(R.id.directBtn)

        autoButton.setOnClickListener {
            var intent = Intent(this, autoGenerate::class.java)
            startActivity(intent)
        }

        directButton.setOnClickListener {
            var intent = Intent(this, direct_generate::class.java)
            startActivity(intent)
        }


//        //뷰 바인딩
//        calendarBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(calendarBinding.root)
//
//        //mainActivity의 calendarBtn을 누르면
//        calendarBinding.calendarBtn.setOnClickListener {
//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.calendar, null)
//            val mbuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//                .setTitle("날짜 선택")
//            val mAlertDialog = mbuilder.show()
//
//            val closeBtn = mDialogView.findViewById<Button>(R.id.closeButton)
//            closeBtn.setOnClickListener {
//
//                mAlertDialog.dismiss()
//            }
//        }
    }
}

















