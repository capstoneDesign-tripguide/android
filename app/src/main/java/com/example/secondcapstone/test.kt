package com.example.secondcapstone


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.secondcapstone.databinding.TestBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import com.navercorp.nid.profile.data.NidProfileResponse

class test : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: TestBinding
    private lateinit var context: Context

    private var clientId = "EBYerfU_Wpnvm801D2jn"
    private var clientSecret = "0IFzyygW3o"
    private var clientName = "네이버 아이디로 로그인"

    private val launcher = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result ->
        when(result.resultCode) {
            RESULT_OK -> {
                // 성공
                Log.d("test0118", "success")
                updateView()
            }
            RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("test0118", "Failure")
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context, "errorCode:$errorCode, errorDesc:$errorDescription", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = TestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        context = this
        // Initialize NAVER id login SDK
        NaverIdLoginSDK.apply {
            showDevelopersLog(true)
            initialize(context, clientId, clientSecret, clientName)
            isShowMarketLink = true
            isShowBottomTab = true
        }

        binding.buttonOAuthLoginImg.setOAuthLogin(object : OAuthLoginCallback {
            override fun onSuccess() {
                updateView()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Log.d("test0118 error", "$errorCode, $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        })

        // 로그인 Launcher
        binding.loginLauncher.setOnClickListener {
            Log.d("test0118","Call loginLauncher")
            NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
            Log.d("test0118","Call loginLauncher next")
            NaverIdLoginSDK.authenticate(context, launcher)
            Log.d("test0118","Call loginLauncher next next")
        }

        // 로그인 Callback
        binding.loginCallback.setOnClickListener {
            NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    Log.d("test0118", "authentication success")
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d("test0118", "authentication Failure")
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.d("test0118", "authentication error")
                    onFailure(errorCode, message)
                }
            })
        }

        // 로그아웃buttonOAuthInit
        binding.logout.setOnClickListener {
            NaverIdLoginSDK.logout()
            updateView()
        }

        // 연동 끊기
        binding.deleteToken.setOnClickListener {
            NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateView()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            })
        }

        // 토큰 갱신
        binding.refreshToken.setOnClickListener {
            NidOAuthLogin().callRefreshAccessTokenApi(object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateView()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // Api 호출
        binding.profileApi.setOnClickListener {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    Toast.makeText(
                        context,
                        "$response",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvApiResult.text = response.toString()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvApiResult.text = ""
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            })
        }


        // 네이버앱 로그인 (Callback)
        binding.loginWithNaverapp.setOnClickListener {
            NaverIdLoginSDK.behavior = NidOAuthBehavior.NAVERAPP
//            NaverIdLoginSDK.naverappIntentFlag = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK  // naverappIntent 생성 시 flag 추가
            NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }

            })
        }

        // Client 정보 변경
        binding.buttonOAuthInit.setOnClickListener {
//            clientId = binding.oauthClientid.text.toString()
//            clientSecret = binding.oauthClientsecret.text.toString()
//            clientName = binding.oauthClientname.text.toString()
            Log.d("test0118", "initializing success")
            NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))

            updateUserData()

        }

        updateUserData()
    }

    private fun updateView() {
        binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
        binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
        binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
        binding.tvType.text = NaverIdLoginSDK.getTokenType()
        binding.tvState.text = NaverIdLoginSDK.getState().toString()
    }

    private fun updateUserData() {
        binding.oauthClientid.setText(clientId)
        binding.oauthClientsecret.setText(clientSecret)
        binding.oauthClientname.setText(clientName)
    }
//test
}