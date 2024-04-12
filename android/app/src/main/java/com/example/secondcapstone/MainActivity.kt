package com.example.secondcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.secondcapstone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    //뷰 바인딩
    private lateinit var calendarBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //여행지 목록 자동 완성
        val destination = resources.getStringArray(R.array.place)
        val autoTextView = findViewById<AutoCompleteTextView>(R.id.destiation)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, destination)
        autoTextView.setAdapter(arrayAdapter)


        //로그인 버튼 클릭 시 액티비티 전환
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
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
            var intent = Intent(this, directGenerate::class.java)
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

















