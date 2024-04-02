package com.example.secondcapstone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

internal class map : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var places: List<Place>
    companion object {
        const val TAG = "MapActivity"
    }

    lateinit var binding: ActivityMapBinding

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var currentMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        this.mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this@map)

    }


    /**
     * onMapReady()
     * Map 이 사용할 준비가 되었을 때 호출
     * @param googleMap
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        //LatLngEntity는 dataclass 이름. 하단에 있음
//        currentMarker = setupMarker(LatLngEntity(37.5562,126.9724))  // default 서울역

        // 변환된 객체 리스트를 반복하며 작업 수행
        for (place in places) {
            // 각 place에 대한 작업을 수행하세요.

            currentMarker = setupMarker(LatLngEntity(place.location.latitude, place.location.longitude), place.displayName.text)
            Log.d("json", "latitud: ${place.location.latitude}, langitude: ${place.location.longitude}")
            currentMarker?.showInfoWindow()
        }

    }


    /**
     * setupMarker()
     * 선택한 위치의 marker 표시
     * @param locationLatLngEntity
     * @return
     */
    private fun setupMarker(locationLatLngEntity: LatLngEntity, displayName: String): Marker? { //LatLngEntity 자료형의 locationLatLngEntity 매개변수. Marker?를 상속받음

        val positionLatLng = LatLng(locationLatLngEntity.latitude!!,locationLatLngEntity.longitude!!)
        val markerOption = MarkerOptions().apply {
            position(positionLatLng)
            title(displayName)
            snippet("서울역 위치") //subtitle 느낌
        }

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL  // 지도 유형 설정
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, 15f))  // 카메라 이동
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))  // 줌의 정도 - 1 일 경우 세계지도 수준, 숫자가 커질 수록 상세지도가 표시됨
        return googleMap.addMarker(markerOption)

    }


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


    /**
     * LatLngEntity data class
     *
     * @property latitude 위도 (ex. 37.5562)
     * @property longitude 경도 (ex. 126.9724)
     */
    data class LatLngEntity(
        var latitude: Double?,
        var longitude: Double?
    )
}
