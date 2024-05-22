package com.example.secondcapstone

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.secondcapstone.databinding.ActivityCalendarNextBinding
import com.example.secondcapstone.databinding.ActivityMainBinding
import com.example.secondcapstone.databinding.LoginActivityBinding
import com.google.gson.JsonObject
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import com.navercorp.nid.profile.data.NidProfileResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class loginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = LoginActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)


        context = this

        //naver_init() //네이버 로그인초기화 -> 네이버 안 씀

        val text_id = findViewById<TextView>(R.id.text_id)
        val editText_id = findViewById<EditText>(R.id.editText_id) //아이디
        val text_pw = findViewById<TextView>(R.id.text_pw)
        val editText_pw = findViewById<EditText>(R.id.editText_pw) //패스워드
        val sign_up_btn = findViewById<TextView>(R.id.sign_up)
        val kakao_login_btn = findViewById<Button>(R.id.kakaoLoginBtn)
        val testBtn = findViewById<Button>(R.id.testbtn)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        testBtn.setOnClickListener { //드로어 레이아웃
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        //비밀번호 텍스트 마스킹
        editText_pw.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val maskedText = maskPassword(p0)
                editText_pw.removeTextChangedListener(this)
                editText_pw.setText(maskedText)
                editText_pw.setSelection(maskedText.length)
                editText_pw.addTextChangedListener(this)
            }

            override fun afterTextChanged(p0: Editable?) {  }

        })

        //토큰, oauth provider 줘야 함
        kakao_login_btn.setOnClickListener { //카카오 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.d("kakaoLogin", "kakao login failed", error)
                }
                else if (token != null) {
                    Log.d("kakaoLogin", "kakao login successed ${token.accessToken}")
                    retroCall(token.accessToken, "kakao")
                }
            }
        }




        editText_id.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_id.setTextColor(Color.parseColor("#99ccff"))
                editText_id.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.lgn_tint)
            } else {
                text_id.setTextColor(Color.parseColor("#000000"))
                editText_id.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            }

        }

        editText_pw.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                text_pw.setTextColor(Color.parseColor("#99ccff"))
                editText_pw.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.lgn_tint)
            } else {
                text_pw.setTextColor(Color.parseColor("#000000"))
                editText_pw.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.black)
            }
        }
        sign_up_btn.setOnClickListener {
            var intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }

        val loginBtn_in_activity = findViewById<Button>(R.id.loginBtn_in_activity)
        loginBtn_in_activity.setOnClickListener {
        //**************************************
        //로그인 성공 시에만 적용하도록 수정해야 함
        //**************************************
            // OkHttpClient 설정
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS) // 연결 타임아웃 설정
                .writeTimeout(120, TimeUnit.SECONDS)   // 쓰기 타임아웃 설정
                .readTimeout(120, TimeUnit.SECONDS)    // 읽기 타임아웃 설정
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://aws-v5-beanstalk-env.eba-vu3h7itj.ap-northeast-2.elasticbeanstalk.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) // OkHttpClient를 Retrofit에 추가
                .build()

            val apiService = retrofit.create(retroTaglist::class.java)
            val informationOf_Login = informationOf_login(
                editText_id.toString(), editText_pw.toString()
            )
            val call = apiService.sendLogin(informationOf_Login)
            call.enqueue(object: retrofit2.Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        //성공 시
                        Log.d("login0522", "success")
                        Log.d("login0522", "$response")
                        Log.d("login0522", "${response.body()?.toString()}")
                    }
                    else{
                        Log.d("login0522", "failed")
                        Log.d("login0522", "$call")
                        Log.d("login0522", "${response.body()?.toString()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("login0522", "Error: ${t.message}")
                }

            }
            )
            isLogin.isLogin = true
            finish()
        }
    }

    //네이버 아이디 로그인 API -> 근데 네이버 안 쓰게 바꿈
//    private fun naver_init() {
//        context = this
//        NaverIdLoginSDK.apply {
//            showDevelopersLog(true)
//            initialize(context, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))
//
//            isShowMarketLink = true
//            isShowBottomTab = true
//        }
//
//        binding.buttonOAuthLoginImg.setOAuthLogin(object : OAuthLoginCallback {
//            override fun onSuccess() {
//                Log.d("test", "naver login success")
//                Log.d("test", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
//                Log.d("test", "client id : " + getString(R.string.naver_client_id))
//                Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
//                Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
//                Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
//                Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())
//                //updateView()
//            }
//
//            override fun onFailure(httpStatus: Int, message: String) {
//                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                Log.d("test", "naver login failed. ErrorCode: ${errorCode}, ErrorDescription: ${errorDescription}")
//            }
//
//            override fun onError(errorCode: Int, message: String) {
//                Log.d("test", "naver login error")
//                onFailure(errorCode, message)
//            }
//
//        })
//
//    }

    private fun maskPassword(s: CharSequence?): String { // CharSequence 객체 s를 매개변수로 받음
        // 최근에 입력된 문자만 표시하고 나머지는 특수문자로 마스킹하기
        val maskedText = StringBuilder()
        s?.let {
            for (i in 0 until it.length - 1) {
                maskedText.append("*")
            }
            if (it.isNotEmpty()) {
                maskedText.append(it[it.length - 1]) // 마지막 문자는 표시
            }
        }
        return maskedText.toString()
    }

    private fun retroCall(accessToken: String, Provider: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com") //서버 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build() //retrofit 객체 생성

        //인터페이스를 Retrofit과 연결. HTTP 요청을 처리할 동적 프록시 객체 생성.
        val apiService = retrofit.create(retroTestInterface::class.java)

        val requestData = LoginRequest( //보낼 데이터 초기화
            accessToken = accessToken,
            Provider = "kakao"
        )

        //sendDataToServer는 apiService 객체의 retroTestInterface에 정의됨
        //LoginRequest 데이터 클래스를 매개변수로 받고, 이건 requestData 변수에 저장돼 있음
        val call = apiService.sendDataToServer(requestData)

        call.enqueue(object : retrofit2.Callback<LoginSuccessResponse> {
            override fun onResponse(
                call: Call<LoginSuccessResponse>,
                response: Response<LoginSuccessResponse>
            ) {
                // 성공적으로 응답을 받았을 때 처리
                val responseData = response.body()
                Log.d("communication", "API communication is successed.")
                Log.d("communication", "${responseData}")
                Toast.makeText(this@loginActivity, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                isLogin.isLogin = true

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("instance_test", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.i("instance_test", "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                    }
                }

                finish()
            }

            override fun onFailure(call: Call<LoginSuccessResponse>, t: Throwable) {
                // 통신 실패 시 처리
                Log.d("communication", "API communication is failed.")
            }
        })
    }

}