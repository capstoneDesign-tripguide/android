package com.example.secondcapstone

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
import com.example.secondcapstone.databinding.ActivityMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.File
import java.io.InputStream
data class Location(val latitude: Double, val longitude: Double)
data class DisplayName(val text: String, val languageCode: String)
data class Place(val id: String, val location: Location, val rating: Double, val displayName: DisplayName)

internal class map : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var places: List<Place>
    companion object {
        const val TAG = "MapActivity"
    }

    lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentMarker: Marker? = null
    // 현재 지도에 추가된 마커를 추적하는 리스트
    val currentMarkers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        places = Gson().fromJson(jsonString, listType) //jsonString을 리스트로 바꿈. onMapReady()에서 마커 추가하는데 사용
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
        val place = places[0]
        currentMarker = setupMarker(LatLngEntity(place.location.latitude, place.location.longitude), place.displayName.text)
        Log.d("json", "latitud: ${place.location.latitude}, langitude: ${place.location.longitude}")
        currentMarker?.showInfoWindow()

        googleMap.setOnMarkerClickListener(this) // 마커 클릭 리스너 설정
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

        for (place in places) {
            if (marker.position.latitude == place.location.latitude && marker.position.longitude == place.location.longitude) {
                return place
            }
        }
        return null
    }

    // 모든 기존 마커를 제거하고, 리스트를 비움
    private fun clearMarkers() {
        currentMarkers.forEach { it.remove() } //리스트의 모든 content에 대해 remove()함수 실행
        currentMarkers.clear() //currentMarkers 리스트 초기화
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
                val place = places[index]

                currentMarker = setupMarker(LatLngEntity(place.location.latitude, place.location.longitude), place.displayName.text)
                Log.d("json", "latitud: ${place.location.latitude}, langitude: ${place.location.longitude}")
                currentMarker?.showInfoWindow()

                googleMap.setOnMarkerClickListener(this) // 마커 클릭 리스너 설정
                Log.d("0409", "$button_id")
            }
            Log.d("0406", "add view.")
            layout.addView(button)
        }
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

