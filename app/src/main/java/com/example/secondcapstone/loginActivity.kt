package com.example.secondcapstone

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.secondcapstone.databinding.ActivityCalendarNextBinding
import com.example.secondcapstone.databinding.ActivityMainBinding
import com.example.secondcapstone.databinding.LoginActivityBinding
import com.google.gson.JsonObject
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class loginActivity : AppCompatActivity(){
    private lateinit var binding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login_activity) //login_activity layout으로 전환

        Log.d("test", "0")
        binding = LoginActivityBinding.inflate(layoutInflater)
        Log.d("test", "0.1")
        setContentView(binding.root)
        Log.d("test", "1")
        val text_id = findViewById<TextView>(R.id.text_id)
        val editText_id = findViewById<EditText>(R.id.editText_id) //아이디
        val text_pw = findViewById<TextView>(R.id.text_pw)
        val editText_pw = findViewById<EditText>(R.id.editText_pw) //패스워드
        val sign_up_btn = findViewById<TextView>(R.id.sign_up)
        val naver_start = findViewById<Button>(R.id.naverLoginBtn)

        Log.d("test", "2")
        //네이버 로그인 정보 초기화
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))

        Log.d("test", "3")
        binding.buttonOAuthLoginImg.setOAuthLogin(object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.d("test","onSuccess")
                updateView()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("test","onFailure")
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    this@loginActivity,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        })

        val retrofit = Retrofit.Builder() //retrofit 객체 생성
            .baseUrl("https://jsonplaceholder.typicode.com") //서버 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(retroTestInterface::class.java)

        val requestData = retroTestRequest( //보낼 데이터 초기화
            ID = editText_id.text.toString(),
            PASSWORD = editText_pw.text.toString()
        )
        val call = apiService.sendDataToServer(requestData)

        call.enqueue(object : retrofit2.Callback<retroTestResponse> {
            override fun onResponse(call: Call<retroTestResponse>, response: retrofit2.Response<retroTestResponse>) {
                // 성공적으로 응답을 받았을 때 처리
                val responseData = response.body()
                Log.d("communication", "API communication is successed.")
                Log.d("communication", "${responseData}")
            }

            override fun onFailure(call: Call<retroTestResponse>, t: Throwable) {
                // 통신 실패 시 처리
                Log.d("communication", "API communication is failed.")
            }
        })


        editText_id.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_id.setTextColor(Color.parseColor("#99ccff"))
                editText_id.backgroundTintList = ContextCompat.getColorStateList(this, R.color.lgn_tint)
            }
            else{
                text_id.setTextColor(Color.parseColor("#000000"))
                editText_id.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
            }

            }

        editText_pw.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_pw.setTextColor(Color.parseColor("#99ccff"))
                editText_pw.backgroundTintList = ContextCompat.getColorStateList(this, R.color.lgn_tint)
            }
            else{
                text_pw.setTextColor(Color.parseColor("#000000"))
                editText_pw.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
            }
        }
        sign_up_btn.setOnClickListener {
            var intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }
    }
    private fun updateView() {
        Log.d("test","updateView")
                Log.d("test", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
                Log.d("test", "client id : " + getString(R.string.naver_client_id))
                Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
                Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
                Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())
    }
}


