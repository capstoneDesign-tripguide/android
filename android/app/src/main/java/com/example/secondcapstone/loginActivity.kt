package com.example.secondcapstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

//class loginActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.login_activity) //login_activity로 화면 전환
//
//        val getData = intent.getStringExtra("data") //mainActivity.kt에서 putExtra로 넣은 데이터를 받음
//        if (getData=="1"){
//            Toast.makeText(this, getData, Toast.LENGTH_LONG).show()
//        }
//    }
//}
class loginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity) //login_activity layout으로 전환

        val getData = intent.getStringExtra("data")
        if (getData == "1"){
            Toast.makeText(this, getData, Toast.LENGTH_LONG).show()
        }

    }
}
