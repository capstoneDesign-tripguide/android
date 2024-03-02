package com.example.secondcapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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



        //네이게이션 요소들
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

        //수동, 자동 일정 생성 버튼 클릭 시 액티비티 전환
        val autoButton = findViewById<Button>(R.id.autoBtn)
        val directButton = findViewById<Button>(R.id.directBtn)

        autoButton.setOnClickListener {
            var intent = Intent(this, calendar::class.java)
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // 메뉴 리소스 인플레이션
        menuInflater.inflate(R.menu.actionbar_btn, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 액션바 버튼 및 메뉴 아이템 클릭 처리
        return when (item.itemId) {
            android.R.id.home -> {
                // 홈 버튼 클릭 시 동작
                // 여기에 필요한 동작 추가
                Log.d("test0131", "click home")
                true
            }
            R.id.action_refresh -> {
                // Refresh 메뉴 아이템 클릭 시 동작
                // 여기에 필요한 동작 추가
                Log.d("test0131", "click refresh")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

















