package com.example.ieum

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val KEY_REPLY="key_reply"

   var target_url="https://www.naver.com/"
    private val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
    private val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101

    private lateinit var  geofencingClient: GeofencingClient
    private lateinit var locationManager: LocationManager
    private lateinit var geofenceHelper: GeofenceHelper

    private lateinit var mMap : GoogleMap
    val geofenceList: MutableList<Geofence> by lazy {
        mutableListOf(
            geofenceHelper.getGeofence("현대백화점", Pair(37.5085864,127.0601149),100f),
            geofenceHelper.getGeofence("삼성역", Pair(37.5094518,127.063603),100f),
            geofenceHelper.getGeofence("광주삼성", Pair(35.205234,126.811794),100f)
        )
    }

    private final val locationCode=2000
    private final val locationCode1 = 2001

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        getMyLocation()
        val samsung = LatLng(35.205234,126.811794)

        addGeofence(samsung)
    }
    private fun getMyLocation() {
        mMap.isMyLocationEnabled=true
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            val currentLatLng = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (currentLatLng != null) {
                Log.d("Main","${currentLatLng.latitude} ${currentLatLng.longitude}!!")
                Toast.makeText(this,"${currentLatLng.latitude} ${currentLatLng.longitude}",Toast.LENGTH_SHORT).show()
            }
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationCode)
            }

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==locationCode){
            if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if((ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
            }

        }
        if(requestCode==locationCode1){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )!=PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
                    return
                }
                Toast.makeText(this,"You can add Geofences", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun test(){}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment)as SupportMapFragment
        mapFragment.getMapAsync(this)

//        웹뷰 생성
        var web= findViewById<WebView>(R.id.web)
        web.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.databaseEnabled = true
            settings.setGeolocationEnabled(true)
            settings.allowFileAccess = true
            // JavaScript 인터페이스 활성화
            addJavascriptInterface(WebAppInterface(this@MainActivity),"WebAppInterface")
        }

        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getString("url") != null && !bundle.getString("url")
                    .equals("", ignoreCase = true)
            ) {
                target_url = bundle.getString("url")!!

                Log.d("main",target_url+"!!")
            }
        }

        web.loadUrl(target_url) // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작



        geofencingClient= LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

        val samsung = LatLng(35.205234,126.811794)
        addGeofence(samsung)

        Log.d(ContentValues.TAG, geofenceList.toString()+"!!")

    }


    private fun addGeofence(p0 : LatLng) {
        val geofence = geofenceHelper.getGeofence("ID", Pair(p0.latitude,p0.longitude),100f)
        val geofenceRequest = geofence?.let{geofenceHelper.getGeofencingRequest(it)}

        val pendingIntent = geofenceHelper.geofencePendingIntent
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return
        }
        geofencingClient.addGeofences(geofenceRequest!!,pendingIntent).run{
            addOnSuccessListener{
                Log.d("Success","Geofence added!!")
            }
            addOnFailureListener{
                Log.d("Failure","Geofence Not added!!")
            }

        }
    }


    private fun checkPermission() {
        val permissionAccessFineLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessFineLocationApproved) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val backgroundLocationPermissionApproved = ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED

                if (!backgroundLocationPermissionApproved) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION
                    )
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION
            )
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        target_url="https://www.naver.com/"
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}

