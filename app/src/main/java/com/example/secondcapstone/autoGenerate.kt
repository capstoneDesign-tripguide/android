package com.example.secondcapstone

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class autoGenerate : AppCompatActivity() {
    var how_long_user_tarvel:Int = -1
    private var jsonArray: JSONArray = JSONArray()
    private var placesList = mutableListOf<List<informationOf_place>>()
    private var finalTravelList = ArrayList<ArrayList<String>?>()
    private val tips = listOf(
        "Tip: 일정을 너무 빡빡하게 짜지 말고, 여유를 두세요. 예상치 못한 상황이 발생할 수 있으니 유연하게 대처하는 것이 좋습니다.",
        "Tip: 주요 관광지 외에도 현지인들이 추천하는 숨은 명소를 방문해 보세요.",
        "Tip: 현지 문화를 존중하고, 예의를 지키세요. 이는 좋은 여행 경험을 만드는 데 중요한 요소입니다.",
        "Tip: 귀중품은 호텔 금고에 보관하고, 필요한 만큼만 소지하세요.",
        "Tip: 교통수단을 이용할 때는 안전한 방법을 선택하고, 목적지를 정확히 파악하세요",
        "Tip: 현지 음식을 맛보는 것은 중요한 여행 경험입니다. 다양한 음식을 시도해 보세요.",
        "Tip: 현지의 문화 행사나 축제에 참여해 보세요. 현지 문화를 깊이 이해할 수 있는 기회가 됩니다.",
        "Tip: 여행 중 발생한 문제점이나 개선할 점을 기록해 두면 다음 여행을 더 잘 준비할 수 있습니다.",
        "Tip: 여행 중 만난 사람들에게 감사의 인사를 전하는 것도 좋은 마무리입니다.",
        "Tip: 여행지에서 사온 작은 선물을 친구나 가족에게 주면 좋은 추억이 될 것입니다."
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auto_generate)
        val dateList = intent.getStringArrayListExtra("dateList") //Calendar에서 받은 날짜
        how_long_user_tarvel = intent.getIntExtra("how_long_user_travel", -1)

        val randomTip = tips.random()
        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        val textview_tip = findViewById<TextView>(R.id.textview_tip)
        textview_tip.text = randomTip

        //이 리스트의 내용을 서버로 넘겨주면 된다.
        var taglist = mutableListOf<String>()  //var 리스트니까 mutableListOf로 선언


        val addBtn = findViewById<Button>(R.id.nextBtn)
        addBtn.setOnClickListener {
            gifImageView.visibility = View.VISIBLE
            textview_tip.visibility = View.VISIBLE
            addBtn.visibility = View.GONE

            Glide.with(this)
                .asGif()
                .load(R.drawable.progressing) // `processing.gif` 파일의 리소스 ID
                .into(gifImageView)

            val planDto = planDto(
                tags = taglist,
                day = how_long_user_tarvel,
                place = place_singleton.selected_place
            )

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

            val json = Gson().toJson(planDto)
            Log.d("jsonPayload", json)

            val apiService = retrofit.create(retroTaglist::class.java)
            CoroutineScope(Dispatchers.Main).launch {

                    val response = sendPlanAsync(apiService, planDto)
                    if (response != null) {
                        Log.d("communication", "Success")
                        Log.d("communication", "$response")
                        Log.d("communication", "${response.toString()}")

                        val jsonString = response.example.toString().trimIndent()
                        jsonArray = JSONArray(jsonString)
                        Log.d("placeList", "$jsonArray")

                        makePlanList(jsonArray)
                        if (planMode.Manual == true) { // 수동 모드면 plan_list.kt로
                            var intent = Intent(this@autoGenerate, plan_list::class.java)
                            for (i in placesList.indices) { // MutableList<List<informationOf_place>>를 인덱스 단위로 put
                                val innerList = placesList[i]
                                val parcelableArray = innerList.toTypedArray()
                                intent.putExtra("placesList_$i", parcelableArray)
                            }
                            intent.putStringArrayListExtra("dateList",ArrayList(dateList)) //여행 날짜
                            intent.putExtra("listSize", placesList.size)
                            startActivity(intent)
                            finish()
                        }
                        else { //자동 모드면 바로 edit_plan.kt로
                            var intent = Intent(this@autoGenerate, edit_plan_list::class.java)

                            for (i in placesList.indices) { // MutableList<List<informationOf_place>>를 인덱스 단위로 put
                                val innerList = placesList[i]
                                val parcelableArray = innerList.toTypedArray()
                                intent.putExtra("placesList_$i", parcelableArray)
                            }
                            intent.putExtra("listSize", placesList.size)

                            // ArrayList<ArrayList<String>?> 타입의 finalTravelList 선언
                            Log.d("final","in auto, $finalTravelList")


                            intent.putExtra("listKey", finalTravelList as Serializable)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Log.e("communication", "Failed with response code: ${response}")
                    }

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

    private fun makePlanList(jsonArray: JSONArray){
        Log.d("placeList", "Called makePlanList")
        if (how_long_user_tarvel > 0) { //여행 일수가 양수일 때만 실행
            placesList = mutableListOf<List<informationOf_place>>()
            Log.d("placeList", "if OK")
            // Iterate over the JSON array
            for (i in 0 until jsonArray.length()) {
                val innerArray = jsonArray.getJSONArray(i)
                val innerPlacesList = mutableListOf<informationOf_place>()

                for (j in 0 until innerArray.length()) {
                    val jsonObject = innerArray.getJSONObject(j)
                    val displayName = jsonObject.getJSONObject("displayName").getString("text")
                    val rating = jsonObject.getDouble("rating")
                    val location = jsonObject.getJSONObject("location")
                    val latitude = location.getDouble("latitude")
                    val longitude = location.getDouble("longitude")

                    // Create an informationOf_place object and add it to the inner list
                    val place = informationOf_place(displayName, rating, latitude, longitude)
                    innerPlacesList.add(place) //i일차 informationOf_place들이 저장된 리스트
                }
                // Add the inner list to the main list
                placesList.add(innerPlacesList) //이제 placesList[i]는 i일자 리스트
            }
            Log.d("placeList", "$placesList")
        }
        return
    }
    private suspend fun sendPlanAsync(apiService: retroTaglist, planDto: planDto): retroTaglistResponse? =
        withContext(Dispatchers.IO) {
            try {
                val call = apiService.sendPlan(planDto)
                return@withContext suspendCoroutine { cont ->
                    call.enqueue(object : retrofit2.Callback<retroTaglistResponse> {
                        override fun onResponse(
                            call: Call<retroTaglistResponse>,
                            response: Response<retroTaglistResponse>
                        ) {
                            if (response.isSuccessful) {
                                cont.resume(response.body())
                            } else {
                                cont.resume(null)
                            }
                        }

                        override fun onFailure(call: Call<retroTaglistResponse>, t: Throwable) {
                            cont.resumeWithException(t)
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("communication", "Error: ${e.message}")
                null
            }
        }
}