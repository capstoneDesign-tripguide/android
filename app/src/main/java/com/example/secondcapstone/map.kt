package com.example.secondcapstone
/*
map.kt 로직
1. onCreate()에서 인텐트로 finalTraveList 받고 json 파싱함
2. n일차 버튼들 생성
3. 맵 실행시킴
4. onMapReady()에서 1일차 마커 생성
5. n일차 버튼 누르면 currentMarker 리스트 초기화
6. + 전역 변수 gloabl_index를 해당 일차로 설정
7. + n일차의 모든 여행지들에 대해 각각 setupMarker() 실행
    -> currentMarkers[global_index]에 마커를 추가함
8. + drawRoute()호출 -> 경로 생성
    -> 근데 한계점: 대중교통 모드만 지원. 나중에 도보 모드, 자동차 모드도 사용자에게 선택시켜야 하나..?
 */


/*이제 해야할 일
0. 나중에 json 받는 로직 추가해야 함
1. 마커 사이의 경로 생성 -> 끝
2. 마커로 순서를 보일 수 있을지 -> 힘들지 않을까..
3. 숙소 위치... 를 고려해야 함. 항상 첫 경로는 숙소 -> 여행지1, 마지막 경로는 여행지n -> 숙소
4. drawRoute에서 api 키 R.string.으로 호출 불가(input stream 이용?)
5. pollyline 제대로 제거 안 됨
    -> 생각해보니 currentPollyline 하나로 추적하고 있었는데 3일차의 경우 폴리라인을 2개 추가함
        그래서 최근에 추가한 하나의 폴리라인만 제거된다. -> 리스트로 만들어서 해결

*/

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.secondcapstone.databinding.ActivityMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import java.io.File
import java.io.InputStream
import java.lang.reflect.Type

data class Location(val latitude: Double, val longitude: Double)
data class DisplayName(val text: String, val languageCode: String)
data class Place(val id: String, val location: Location, val rating: Double, val displayName: DisplayName)

internal class map : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
//    private lateinit var places: List<Place>
    private lateinit var places: List<List<Place>>
    private var global_index: Int = -1
    companion object {
        const val TAG = "MapActivity"
    }

    lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentMarker: Marker? = null

    // 현재 지도에 추가된 마커를 추적하는 리스트
    val currentMarkers = mutableListOf<Marker>()

    // 얘는 현재 폴리 라인(경로)을 추적
    var currentPolyline: Polyline? = null
    val polylines = mutableListOf<Polyline?>() //currentPolyline들을 추가할 리스트. 일자 변경 시 clearPollyLines()가 얘를 초기화함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //************************************************************************
        //*****************     추후   꼭   수정  **********************************
        //************************************************************************
//        if (planMode.Automatic == true) {
//            서버에서 json데이터 가져오는 로직
//        }
        //************************************************************************
        //*****************     추후   꼭   수정  **********************************
        //************************************************************************

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val finalTravelList =
            intent.getSerializableExtra("listKey") as? ArrayList<ArrayList<String>?> //serializable을 ArrayList로 받음
        Log.d("map.kt", "$finalTravelList")
        //        val pt_jsonString = File("C:\Users\yeoyeoungkyu\Desktop\secondCapstone\app\src\main\res\raw\sample.json").readText()
        /*        res/raw 디렉토리의 파일은 앱의 리소스로 패키지화되어 디바이스에 설치될 때
                  압축되어 저장되므로 File 모듈로 읽어올 수 없다고 한다
                  따라서 밑과 같이 inputStream을 이용해야 함 (자바와 안드로이드에서 데이터를 바이트 단위로 읽어오는 데 사용되는 클래스)
        */
        val inputStream: InputStream = resources.openRawResource(R.raw.sample)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Log.d("json", "$jsonString")


        val listType = object : TypeToken<List<Place>>() {}.type
//        places = Gson().fromJson(jsonString, listType) //jsonString을 리스트로 바꿈. onMapReady()에서 마커 추가하는데 사용
        val type: Type = object : TypeToken<List<List<Place>>>() {}.type
        places = Gson().fromJson(jsonString, type)

        Log.d("0409", "$places")
        /*
        예시 json 기반으로 위 로그의 값은 이렇게 나옴
        [
        Place(id=ChIJw8UIcGCifDURB0MtY-xAUqM,
            location=Location(latitude=37.557839099999995,
            longitude=126.9697808),
            rating=4.1,
            displayName=DisplayName(text=Seoullo 7017, languageCode=en)),

         Place(id=sample1_id,
            location=Location(latitude=37.55883909999999,
            longitude=126.9707808),
            rating=4.2,
            displayName=DisplayName(text=sample1, languageCode=en)),

        Place(id=sample2_id,
            location=Location(latitude=37.5568391,
            longitude=126.9687808),
            rating=4.3,
            displayName=DisplayName(text=sample2, languageCode=en))
        ]
         */
        this.mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this@map)
        Log.d("0406", "$finalTravelList before called")
        createBtn(finalTravelList)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        //LatLngEntity는 dataclass 이름. 하단에 있음
        //currentMarker = setupMarker(LatLngEntity(37.5562,126.9724))  // default 서울역

        // 변환된 객체 리스트를 반복하며 작업 수행

        // places의 각 요소마다 마커 추가
        // 처음엔 1일차 마커만 추가하고, 버튼들의 클릭 리스너에서 마커 clear 후 해당 일차 마커들 추가하면 될 것 같은데..

        //처음 Map Ready 시 1일차 마커 생성
        for(place in places[0]) {
            currentMarker = setupMarker(
                LatLngEntity(place.location.latitude, place.location.longitude),
                place.displayName.text
            )
            Log.d(
                "json",
                "latitud: ${place.location.latitude}, langitude: ${place.location.longitude}"
            )
            currentMarker?.showInfoWindow()

            googleMap.setOnMarkerClickListener(this) // 마커 클릭 리스너 설정
        }
    }


    private fun setupMarker(locationLatLngEntity: LatLngEntity, displayName: String): Marker? {
        //LatLngEntity 자료형의 locationLatLngEntity 매개변수. Marker?를 상속받음

        val positionLatLng = LatLng(locationLatLngEntity.latitude!!,locationLatLngEntity.longitude!!)
        val markerOption = MarkerOptions().apply {
            position(positionLatLng)
            title(displayName)
            snippet("서울역 위치") //subtitle 느낌
        }

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL  // 지도 유형 설정
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, 15f))  // 카메라 이동
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))  // 줌의 정도 - 1 일 경우 세계지도 수준, 숫자가 커질 수록 상세지도가 표시됨

        val marker =  googleMap.addMarker(markerOption)
        currentMarkers.add(marker!!) //나중에 이 리스트를 토대로 clearMarker 함수에서 마커를 지움

        return marker

    }
    override fun onMarkerClick(marker: Marker): Boolean {
        Log.d("marker", "clicked.")

        val place = findPlaceByMarker(marker)
        if (place != null) {
            val message = "이름: ${place.displayName.text}\n별점: ${place.rating}"
            showAlertDialog(place.displayName.text, place.rating)
        }
        return false

        /*
        false 리턴 -> 이벤트를 소비하지 않았다고 리턴함
        이벤트를 소비했다면 추가적인 이벤트 처리가 이루어지지 않는다고 함
        true 리턴하면 마커 클릭 시 title이랑 snippet 사라짐
        */
    }

    //onMarkerClick()에서 마커 클릭 시 상세 정보 창 생성해주는 함수
    private fun showAlertDialog(displayName: String, rating: Double) {
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.marker_detail, null)

        //alertDialog 생성
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        //이 두 코드 없으면 background 설정해도, 그 밖으로 테두리 각지게 튀어나옴
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val title_textView = view.findViewById<TextView>(R.id.title)
        title_textView.text = displayName

        val rating_textView = view.findViewById<TextView>(R.id.rating)
        rating_textView.text = rating.toString()

        val confirm_btn = view.findViewById<Button>(R.id.confirm)
        confirm_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    // 마커와 연결된 place를 찾는 함수
    private fun findPlaceByMarker(marker: Marker): Place? {
        if(global_index > -1) { //global_index가 전역 변수라 혹시나 생길 에러 방지
            for (place in places[global_index]) {
                if (marker.position.latitude == place.location.latitude && marker.position.longitude == place.location.longitude) {
                    return place
                }
            }
        }
            return null

    }

    // 모든 기존 마커를 제거하고, 리스트를 비움
    private fun clearMarkers() {
        currentMarkers.forEach { it.remove() } //리스트의 모든 content에 대해 remove()함수 실행
        currentMarkers.clear() //currentMarkers 리스트 초기화
    }

    private fun clearPolylines(){
        polylines.forEach { it?.remove()}
        polylines.clear()

    }

    //여행 일자만큼 'n일차 마커 보기' 버튼 생성
    private fun createBtn(finalTravelList: ArrayList<ArrayList<String>?>?) {
        Log.d("0406", "$finalTravelList")
        val layout = findViewById<LinearLayout>(R.id.parent_linear)

        finalTravelList!!.forEachIndexed { index, locations ->
            Log.d("0406", "repeat")
            val button_id = View.generateViewId()
            val button = Button(this).apply {
                text = "${index + 1}일차 여행지 보기"
                setBackgroundResource(R.drawable.btn_repple_ex)
                textSize = 20f
                typeface = resources.getFont(R.font.jua_ttf)
                setTextColor(ContextCompat.getColor(context, R.color.white)) // 여기에 원하는 텍스트 색상의 리소스 ID를 넣으세요
                // LinearLayout.LayoutParams를 사용하여 마진 설정 가능
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 25, 0, 0)
                }
                this.layoutParams = layoutParams
            }
            button.id = button_id //각 버튼의 아이디 추가. button_id는 View.generateViewId()로 생성했음


            button. setOnClickListener { //각 버튼의 클릭 리스너 추가
                clearMarkers() //기존 마커 초기화
                Log.d("0412", "start to clear currentPollyline")

                clearPolylines() //기존 폴리 라인들 초기화
//                currentPolyline?.remove() // 폴리라인 초기화
//                Log.d("0412", "currentPollyline is removed. and is $currentPolyline")
//
//                currentPolyline = null // 폴리라인 초기화2
//                Log.d("0412", "currentPollyline is deallocated. and is $currentPolyline")

                //n일차 버튼 클릭 시 global_index를 n으로 설정, 각 n번째 마커 설정들 출동
                global_index = index
                if (global_index > -1){ //global_index가 전역 변수라 혹시나 생길 에러 방지
                    for (place in places[global_index]) {
                        currentMarker = setupMarker(
                            LatLngEntity(place.location.latitude, place.location.longitude),
                            place.displayName.text
                        )
                        Log.d(
                            "json",
                            "latitud: ${place.location.latitude}, langitude: ${place.location.longitude}"
                        )
                        currentMarker?.showInfoWindow()

                        googleMap.setOnMarkerClickListener(this) // 마커 클릭 리스너 설정
                        Log.d("0409", "$button_id")
                    }
                }

                //경로 그리기
                //예를 들어 1일차의 여행지는 세 곳이라고 해보자
                //places[global_index].size = 3
                //그럼 i는 1부터 2까지
                //places[global_index][0] ~ places[global_index][1]
                //places[global_index][1] ~ places[global_index][2]

                for (i in 1 until (places[global_index].size)){
                    var origin: LatLngEntity = LatLngEntity(places[global_index][i-1].location.latitude, places[global_index][i-1].location.longitude)
                    var destination: LatLngEntity = LatLngEntity(places[global_index][i].location.latitude, places[global_index][i].location.longitude)
                    //1일차, 2일차엔 로그 안 찍힘
                    Log.d("0410","$origin")
                    Log.d("0410","$destination")
                    drawRoute(googleMap, origin, destination, "AIzaSyByXRkTKRM3O8P1Sq7fbI4I60UMVdovReg")               }
            }
            Log.d("0406", "add view.")
            layout.addView(button)
        }
    }
    private fun drawRoute(map: GoogleMap, origin: LatLngEntity, destination: LatLngEntity, apiKey: String) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}&" +
                "destination=${destination.latitude},${destination.longitude}&" +
                "mode=transit&" + // 예시로 자동차 모드 선택
                //deafult: driving 모드. //걷기, 대중교통, 자동차
                "key=$apiKey"
        Log.d("0410", "$url")


        val directionsRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.d("0411", "response is ${response}")
            Log.d("0411", "origin: $origin, destination: $destination")
            Log.d("0411", "${url}")
            val routes = response.getJSONArray("routes")
            if (routes.length() > 0) { // routes 배열이 비어 있지 않은지 확인
                val route = routes.getJSONObject(0)
                val overviewPolyline = route.getJSONObject("overview_polyline")
                val polyline = overviewPolyline.getString("points")
                val decodedPath = PolyUtil.decode(polyline)
                Log.d("0411", "$decodedPath")

                //지도에 폴리 라인을 그리고, 반환된 객체를 currentPolyline에 저장
                currentPolyline = map.addPolyline(PolylineOptions().addAll(decodedPath))
                polylines.add(currentPolyline)
                Log.d("0412", "currentPolyline added. and is $currentPolyline")
            } else {

                Log.e("drawRoute", "No routes found.")

            }
        }, { error ->
            Log.d("0411", "draw routes error")
            error.printStackTrace()
        })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }



    data class LatLngEntity(
        var latitude: Double?,
        var longitude: Double?
    )

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }


}

