package com.example.secondcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.secondcapstone.databinding.ActivityMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

internal class map : AppCompatActivity(), OnMapReadyCallback {

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
        currentMarker = setupMarker(LatLngEntity(37.5562,126.9724))  // default 서울역
        currentMarker?.showInfoWindow()
    }


    /**
     * setupMarker()
     * 선택한 위치의 marker 표시
     * @param locationLatLngEntity
     * @return
     */
    private fun setupMarker(locationLatLngEntity: LatLngEntity): Marker? { //LatLngEntity 자료형의 locationLatLngEntity 매개변수. Marker?를 상속받음

        val positionLatLng = LatLng(locationLatLngEntity.latitude!!,locationLatLngEntity.longitude!!)
        val markerOption = MarkerOptions().apply {
            position(positionLatLng)
            title("위치")
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

/** 공식 문서 예제 코드
package com.example.secondcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions


class map : AppCompatActivity(){

private val places: List<Place> by lazy {
PlacesReader(this).read()
}
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

setContentView(R.layout.activity_map)

val mapFragment = supportFragmentManager.findFragmentById(
R.id.map_fragment
) as? SupportMapFragment
mapFragment?.getMapAsync { googleMap ->
addMarkers( googleMap)
}
}
private fun addMarkers(googleMap: GoogleMap) {
places.forEach { place ->
val marker = googleMap.addMarker(
MarkerOptions()
.title(place.name)
.position(place.latLng)
)
}
}
}
 */

/**
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <fragment
        class="com.google.android.gms.maps.SupportMapFragment"
        android:id="@+id/map_fragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
 **/